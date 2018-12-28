package com.org.ui.stepDefinitions;

import org.jruby.util.log.LoggerFactory;

import com.org.ui.base.SeleniumUtils;
import com.org.ui.pages.crmpropage;
import org.slf4j.Logger;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class crmprostepdefination {
	
	private crmpropage crmpage = new crmpropage();
	SeleniumUtils util = new SeleniumUtils();
	@Given("^I clicked on username and password$")
	public void i_clicked_on_username_and_password()  {
		crmpage.crmlogin("bhaskerptg","bhaskerptg");

	}


	@When("^click on submit button$")
	public void click_on_submit_button()  {
		
	}

	@When("^close the browser$")
	public void close_the_browser() {
		util.closeCurrentWindowOfheBrowser();

	}
}