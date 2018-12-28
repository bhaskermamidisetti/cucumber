package com.org;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = {"src/main/java/com/org/ui/features"},
        tags = {"@crmTestCase"},
        snippets = SnippetType.CAMELCASE
)
public class TestSuiteRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestSuiteRunner.class);

    /**
     * Define runtime properties for test execution
     */
    @BeforeClass
    public static void setupProperties() {

        System.setProperty("ExecutionEnv", "Stage");   // Environment to execute against

        System.setProperty("BrowserEnv", "local");     // "local" or "saucelab"

        // Default is FireFox
        System.setProperty("BrowserType", "chrome");  // for "ie" or "firefox" or "chrome" or "edge" or "headless chrome" or "headless firefox"

        System.setProperty("BrowserVersion", "61"); //Firefox version

        logger.info("Environment to execute is :: " + System.getProperty("ExecutionEnv"));

    }


}


