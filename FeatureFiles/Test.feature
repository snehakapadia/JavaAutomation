@tag
Feature: Testing a basic scenario in Flink
		
	@TestScenario
  Scenario: Testing a basic scenario in Flink
    Given User navigates to "Flink" webpage
    Then User "can" see "Current_temperature" text
    And User "can" see "Moisturizers" text
    And User "can" see "Sunscreens" text
    When User selects "Buy_moisturizers" or "Buy_sunscreens" based on temparture
    Then User "can" see "MoisturizersOrSunscreens" text
    And User "can" see "Cart" text
    When User select least expensive "SPF50OrAloe" item
    And User select least expensive "SPF30OrAlmond" item
    And User clicks on "Cart"
    Then User verifies the "ItemAndPrice" table
    And User verifies the "Total"
    When User clicks on "PayWithCard"
    And User switches to "Stripe" frame
		And User enters "test@gamil.com" in "emailId"
		And User enters "4242 4242 4242 4242" in "card_number"
		And User enters "10/25" in "monthYear"
		And User enters "007" in "cvc"
		And User enters "12345" in "zipCode"
		And User click on "PayInr"
		And User switches out of "Stripe" frame
		Then User "can" see "paymentSuccess" text
		And User "can" see "paymentText" text