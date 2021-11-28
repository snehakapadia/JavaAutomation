I have created a BDD Cucumber framework for the given tasks.
You can go to config.properties and mention the browser that you want to run tests on, for now I have configured Chrome and Firefox.
Below are paths for important folders:
Feature Files: FeatureFiles
StepDefinations: src/test/java/FlinkAutomation/FlinkAutomation
TestRunner: src/test/java/FlinkAutomation/FlinkAutomation
I have pom.xml for dependency injection. You cab find the pom.xml in the root directory of the project.
Execution Reports will be generated in 'target/cucumber/index.html' (The report is a basic cucumber html report, it can be beautified through plugins).

To Run the tests:
1. Git clone the project on your machine to the desired location.
2. On the Terminal, go to the root directory of the project and execute 'mvn test'. Optionally, we can also execute the TestRunner.java class to execute the tests.

Note: The tests will run in headless mode. If you want to execute the tests in head mode you need to change line number 71/line number 83 (based on the browser selected from config.properties) in Stepdefinations.java to 'options.addArguments("start-maximized"); '
I have created 2 feature files and each has a single scenario, both scenarios are exact copy as I wanted to give an example of parallel execution.