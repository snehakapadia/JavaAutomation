package FlinkAutomation.FlinkAutomation;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "FeatureFiles"
		,glue={"FlinkAutomation.FlinkAutomation"}
		,monochrome = true
		,plugin = {"pretty", "html:target/cucumber"}
		//,tags = {"@TestScenario"}
		)

public class TestRunner {

}
