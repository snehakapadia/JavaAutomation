package FlinkAutomation.FlinkAutomation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class StepDefinations {
	
	Properties config = new Properties();
	Properties xpath = new Properties();;
	WebDriver driver = null;
	HashMap<String, String> gHash = new HashMap<String, String>();
	
	@Before
	public void setup()
	{
		String configFileName = "config.properties";
		
		try {
			config.load(new FileInputStream(configFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		configFileName = "xpath.properties";
		 
		try {
			xpath.load(new FileInputStream(configFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Given("User navigates to {string} webpage")
	public void hitURL(String appName)
	{
		switch (config.getProperty("Browser").toLowerCase())
		{
		case "chrome":
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); 
			options.addArguments("enable-automation"); 
			options.addArguments("--no-sandbox"); 
			options.addArguments("--disable-infobars");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-browser-side-navigation"); 
			options.addArguments("--disable-gpu"); 
			driver = new ChromeDriver(options);
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions optionsf = new FirefoxOptions();
			optionsf.addArguments("--headless"); 
			optionsf.addArguments("enable-automation"); 
			optionsf.addArguments("--no-sandbox"); 
			optionsf.addArguments("--disable-infobars");
			optionsf.addArguments("--disable-dev-shm-usage");
			optionsf.addArguments("--disable-browser-side-navigation"); 
			optionsf.addArguments("--disable-gpu"); 
			driver = new FirefoxDriver(optionsf);
		}
		
		driver.get(config.getProperty(appName));
	}
	
	@Then("User {string} see {string} text")
	public void validate(String visible, String text)
	{
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		w.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath.getProperty(text))));
		w.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath.getProperty(text)))));
		if(visible.equalsIgnoreCase("can"))
		{
			assertEquals(driver.findElement(By.xpath(xpath.getProperty(text))).isDisplayed(), true);
		}
		else if(visible.equalsIgnoreCase("cannot"))
		{
			assertNotEquals(driver.findElement(By.xpath(xpath.getProperty(text))).isDisplayed(), true);
		}
	}
	
	@When("User clicks on {string}")
	public void clickAnything(String name)
	{
		driver.findElement(By.xpath(xpath.getProperty(name))).click();
	}
	
	@When("User click on {string}")
	public void clickJS(String name)
	{
		//driver.findElement(By.xpath(xpath.getProperty(name))).click();
		WebElement element = driver.findElement(By.xpath(xpath.getProperty(name)));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
	}
	
	
	@When("User switches to {string} frame")
	public void switchToFrame(String name)
	{
//		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
//		w.until(ExpectedConditions.fr (xpath.getProperty(name)));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.switchTo().frame(xpath.getProperty(name));
	}
	
	@When("User switches out of {string} frame")
	public void switchOutOfFrame(String name)
	{
		driver.switchTo().defaultContent();
	}
	
	@When("User picks up text from {string}")
	public void pickText(String text)
	{
		String pickedUpText = driver.findElement(By.xpath(xpath.getProperty(text))).getText();
		gHash.put(text, pickedUpText);
	}
	
	@When("User selects {string} or {string} based on temparture")
	public void selectMoistOrSun(String moist, String sun)
	{
		String temperature = driver.findElement(By.xpath(xpath.getProperty("temperature"))).getText();
		temperature = temperature.split(" ")[0];
		if(Integer.parseInt(temperature) < 19)
		{
			driver.findElement(By.xpath(xpath.getProperty(moist))).click();
		}
		else if(Integer.parseInt(temperature) > 34)
		{
			driver.findElement(By.xpath(xpath.getProperty(sun))).click();
		}
		else
		{
			System.out.println("End Test as the tenperature is between 19 to 34 " + temperature);
		}
	}
	
	@When("User select least expensive {string} item")
	public void selectItem(String itemName)
	{
		List<WebElement> items = driver.findElements(By.xpath(xpath.getProperty(itemName)));
		//Craeting an Array and go through all elements in the List to find the Item with Least price
		int[] prices = new int[items.size()];
		int counter = 0;
		for(WebElement item:items)
		{
			String price = item.findElement(By.xpath("./following-sibling::p")).getText();
			price = price.split(" ")[price.split(" ").length - 1].trim();
			prices[counter] = Integer.parseInt(price);
			counter++;
		}
		Arrays.sort(prices);
				
		String leastExpensiveXpath = xpath.getProperty("Price") + "[contains(text(),'Price') and contains(text(),'" + prices[0] + "')]";
		//Storing the price & name in global hashmap
		gHash.put(driver.findElement(By.xpath(leastExpensiveXpath + "/preceding-sibling::p")).getText() + "name", driver.findElement(By.xpath(leastExpensiveXpath + "/preceding-sibling::p")).getText());
		gHash.put(driver.findElement(By.xpath(leastExpensiveXpath + "/preceding-sibling::p")).getText() + "price",prices[0] + "");
		
		driver.findElement(By.xpath(leastExpensiveXpath + "/following-sibling::button")).click();
	}
	
	@Then("User verifies the {string} table")
	public void verifyItemPriceTable(String text)
	{
		int counter = 1;
		String name = "";
		for(WebElement col:driver.findElements(By.xpath(xpath.getProperty(text) + "//td")))
		{
			String actual = col.getText();
			String expected = null;
		
			if(counter % 2 == 0)
			{
				expected = gHash.get(name + "price");
				name = "";
			}
			else
			{
				expected = gHash.get(actual + "name");
				name = actual;
			}
			assertEquals(expected, actual);
			counter++;
		}
	}
	
	@Then("User verifies the {string}")
	public void verifyTotal(String total)
	{
		String actualValue = driver.findElement(By.xpath(xpath.getProperty(total))).getText();
		actualValue = actualValue.split(" ")[actualValue.split(" ").length - 1];
		int actual = Integer.parseInt(actualValue);
		int expected = 0;
		for(String key:gHash.keySet())
		{
			if(key.contains("price"))
				expected = expected + Integer.parseInt(gHash.get(key));
		}
		assertEquals(actual,expected);	
	}
	
	@When("User enters {string} in {string}")
	public void enterData(String value, String box)
	{
		String[] val = value.split("");
		for(int i = 0;i<val.length;i++)
		{
			driver.findElement(By.xpath(xpath.getProperty(box))).sendKeys(val[i]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//driver.findElement(By.xpath(xpath.getProperty(box))).sendKeys(value);
	}
	
	
	@After
	public void close()
	{
		driver.close();
		
	}

}
