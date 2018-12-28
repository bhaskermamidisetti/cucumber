package com.org.ui.stepDefinitions;

import com.org.ui.pages.FlipkartPage;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * @author Gomasa Chandra Shekhar on 10/22/2018
 * @project CucumberWithSerenity
 */
public class FlipkartSteps {

    private static final Logger logger = LoggerFactory.getLogger(FlipkartSteps.class);

    private FlipkartPage flipkartpage = new FlipkartPage();

    @And("^I clicked on search products field$")
    public void iClickedOnSearchProductsField() {
        flipkartpage.clickOnSearchProductField();
    }

    @When("^I entered any product name in search box$")
    public void iEnteredAnyProductNameInSearchBox() {
            flipkartpage.enterProductName("iphone 6s phone");
    }

    @And("^I clicked on Search icon$")
    public void iClickedOnSearchIcon() {
        flipkartpage.clickOnSearchIcon();
    }

    @Then("^I should see list of products$")
    public void iShouldSeeListOfProducts() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals( flipkartpage.verifyTest(), "Showing 1 – 24 of 32 results for \"iphone 6s mobile\"");
    }
}
