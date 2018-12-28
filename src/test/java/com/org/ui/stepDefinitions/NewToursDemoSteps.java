package com.org.ui.stepDefinitions;

import com.org.ui.pages.NewTourseDemoPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewToursDemoSteps {

   private static final Logger logger = LoggerFactory.getLogger(NewToursDemoSteps.class);

    private NewTourseDemoPage logpage = new NewTourseDemoPage();

    @Given("^User navigates to \"([^\"]*)\"$")
    public void user_navigates_to(String arg1) {
        logpage.openBrowser(arg1);
    }

    @Given("^I enter \"([^\"]*)\" into userName and \"([^\"]*)\" into password$")
    public void iEnterIntoUserNameAndIntoPassword(String arg1, String arg2) {
        logpage.enterUserNamePassword(arg1, arg2);
    }

    @When("^I click on Login button$")
    public void i_click_on_Login_button() {
        logpage.clickOnLogin();

    }

    @Then("^I should see SignOff link$")
    public void i_should_see_Login_page() throws Exception {
        Assert.assertTrue(logpage.VerifySignOffLinkDisplayed());

    }

}
