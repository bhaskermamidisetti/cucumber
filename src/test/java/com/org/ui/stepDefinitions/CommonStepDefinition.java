package com.org.ui.stepDefinitions;

import com.org.ui.base.SeleniumUtils;
import com.org.ui.utility.Hooks;
import com.org.ui.utility.dataStorage;
import com.org.ui.utility.readXMLdata;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/*import com.epam.ui.pages.MailInator_ConfirmRegistrationEmailPage;
import com.epam.ui.pages.MailInator_InboxPage;*/

public class CommonStepDefinition {

	SeleniumUtils page = new SeleniumUtils();
	String winHandleBefore;
	String winHandleNew;

	public CommonStepDefinition() {
		//
	}

	@Given("^I am at home page of the application$")
	public void i_am_at_home_page_of_the_application() {
		String page_url = readXMLdata.getTestData(dataStorage.getPortalName(), "AppURL");
		page.getUrl(page_url);
	}

	private static final String SUB_HEADING_IS_NOT_DISPLAYED_ON_THE_PAGE = "\" sub heading is not displaying on the page";

	@Given("^I get the following details \"([^\"]*)\" from xml file$")
	public void i_get_the_following_details_for_the_member_from_xml_sheet(String BinId, List<String> inputKeys)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		String xmlRoot = dataStorage.getPortalName() + "/BinDetails/" + BinId + "/";
		// Loop through each key provided in input List, invoke
		// "dataStorage.set{key}()"
		// passing in data retrieved from XML file
		for (String key : inputKeys) {
			Class<?> cls = Class.forName("com.epam.ui.utility.dataStorage");
			Method method = cls.getDeclaredMethod("set" + key, String.class);
			dataStorage.setCustomErrmsg(
					"Member XML Data::" + xmlRoot + "/" + key + " = " + readXMLdata.getTestData(xmlRoot, key));
			method.invoke(null, readXMLdata.getTestData(dataStorage.getPortalName() + "/BinDetails/" + BinId, key));
		}
	}

	@Given("^I get the following details for the login \"([^\"]*)\" from xml file$")
	public void i_get_the_following_details_for_the_login_from_xml_sheet(String member, List<String> inputKeys)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		String xmlRoot = dataStorage.getPortalName() + "/Logins/" + member;
		// Loop through each key provided in input List, invoke
		// "dataStorage.set{key}()"
		// passing in data retrieved from XML file
		for (String key : inputKeys) {

			String testDataFromXML = readXMLdata.getTestData(xmlRoot, key);

			dataStorage.setCustomErrmsg("Login XML Data::" + xmlRoot + "/" + key + " = " + testDataFromXML);

			Class<?> cls = Class.forName("com.epam.ui.utility.dataStorage");
			Method method = cls.getDeclaredMethod("set" + key, String.class);
			method.invoke(null, testDataFromXML);
		}
	}

	@When("^I click browser back button$")
	public void i_click_browser_back_button() throws Throwable {
		page.clickBrowserBackButton();
	}

	@When("^I click \"([^\"]*)\" link$")
	public void i_click_link(String link) throws Throwable {
		page.clickLink(link);
	}

	@When("^I click on \"([^\"]*)\" button$")
	public void i_click_on_button(String buttonName) throws Throwable {
		page.clickButtonBySubmitTypeAndName(buttonName);
		Thread.sleep(1000);

	}

	/**
	 * --------------------------------------- Thens
	 * -----------------------------------------
	 **/
	@Then("^I should see the correct browser url for \"([^\"]*)\" link$")
	public void iShouldSeeTheCorrectBrowserUrlForLink(String arg1) throws Throwable {
		throw new PendingException();
	}

	@Then("^I should see the \"([^\"]*)\" page heading$")
	public void i_should_see_the_page_heading(String heading) throws Throwable {
		Assert.assertTrue("\"" + heading + "\" heading is not displaying on the page",
				page.verifythePageHeadingByH1Tag(heading));
	}

	@Then("^I should see the sub-heading \"([^\"]*)\"$")
	public void i_should_see_the_sub_heading(String subHeading) throws Throwable {
		Assert.assertTrue("\"" + subHeading + SUB_HEADING_IS_NOT_DISPLAYED_ON_THE_PAGE,
				page.verifytheSubHeadingByH2Tag(subHeading));
	}

	@Then("^I should see the form heading \"([^\"]*)\"$")
	public void i_should_see_the_form_heading(String subHeading) throws Throwable {
		Assert.assertTrue("\"" + subHeading + SUB_HEADING_IS_NOT_DISPLAYED_ON_THE_PAGE,
				page.verifytheSubHeadingByPTag(subHeading));
	}

	@Then("^I should see the form span content \"([^\"]*)\"$")
	public void i_should_see_the_form_span_content(String subHeading) throws Throwable {
		Assert.assertTrue("\"" + subHeading + SUB_HEADING_IS_NOT_DISPLAYED_ON_THE_PAGE,
				page.verifytheSubHeadingByspan(subHeading));
	}

	@Then("^I should see a new tab opened and switch to it$")
	public void i_should_see_a_new_tab_opened_and_switch_to_it() throws Throwable {

		// may want more general-purpose implementation
		SeleniumUtils.mediumWait.get().until(ExpectedConditions.numberOfWindowsToBe(2));
		WebDriver driver = Hooks.getDeviceDriver();
		winHandleBefore = driver.getWindowHandle();
		Set<String> windows = driver.getWindowHandles();
		Assert.assertTrue("New browser window is not opened", windows.size() > 1);
		for (String winHandle : driver.getWindowHandles()) {
			winHandleNew = winHandle;
			driver.switchTo().window(winHandle);
		}
		driver.switchTo().defaultContent();
		page.waitForPageLoad(driver);

	}

	@Then("^I should see a new tab opened for \"([^\"]*)\" and switch to it$")
	public void i_should_see_a_new_tab_opened_and_switch_to_that(String arg1) throws Throwable {
		// Give new tab two seconds to launch and populate
		Thread.sleep(2000);

		// may want more general-purpose implementation
		SeleniumUtils.mediumWait.get().until(ExpectedConditions.numberOfWindowsToBe(2));
		WebDriver driver = Hooks.getDeviceDriver();
		Set<String> windows = driver.getWindowHandles();
		Assert.assertTrue("New browser window is not opened", windows.size() > 1);
		boolean found = false;
		for (String winHandle : driver.getWindowHandles()) {

			driver.switchTo().window(winHandle);
			page.waitForPageLoad(driver);
			if (driver.getCurrentUrl().contains(arg1)) {
				found = true;
				break;
			}
		}
		Assert.assertTrue("Failed to find tab with [" + arg1 + "], last tab url [" + driver.getCurrentUrl() + "]",
				found);
	}

	@Then("^I close the new tab and switch back to previous tab$")
	public void i_close_the_new_tab_and_switch_back_to_previous_tab() throws Throwable {
		WebDriver driver = Hooks.getDeviceDriver();
		Set<String> windows = driver.getWindowHandles();
		Assert.assertTrue("More than 1 browser windows are not there to close", windows.size() > 1);
		/*
		 * String winHandleBefore = driver.getWindowHandle(); for(String winHandle :
		 * driver.getWindowHandles()){
		 */
		if (driver.getWindowHandle().equals(winHandleNew)) {
			driver.close();
			driver.switchTo().window(winHandleBefore);
			driver.switchTo().defaultContent();
		}

	}

	@Then("^I open a new window tab in browser$")
	public void iOpenANewWindowTabInBrowser() throws Throwable {
		page.openNewTab();
	}

	@Then("^I switch back to previous tab in browser$")
	public void iSwitchBackToPreviousTabInBrowser() throws Throwable {
		page.switchBackToPreviousTab();
	}

	@Then("^I switch to next tab in browser$")
	public void iSwitchBackToNextTabInBrowser() throws Throwable {
		page.switchToNextTab();
	}

	@Then("^I refresh the current window tab$")
	public void iRefreshTheCurrentWindowTab() throws Throwable {
		page.refreshPage();
		page.switchToDefaultContent();
	}

	@Then("^I should switch to next tab$")
	public void i_should_switch_to_next_tab() throws Throwable {
		page.switchToNextTab();
	}

	@Given("^I switch back to previous tab$")
	public void i_switch_back_to_previous_tab() throws Throwable {
		page.switchBackToPreviousTab();
	}

	@Then("^I should see a \"([^\"]*)\" button$")
	public void i_should_see_a_button(String buttonName) throws Throwable {
		Assert.assertTrue(page.verifyButtonBySubmitTypeAndName(buttonName));
	}

	@Then("^I click on \"([^\"]*)\" partial link$")
	public void i_click_on_partial_link(String partialLink) throws Throwable {
		page.clickPartialLink(partialLink);
		Thread.sleep(500);
	}

	@Then("^I see a link for \"([^\"]*)\"$")
	public void iShouldSeeALinkFor(String linkText) throws Throwable {
		Assert.assertTrue(page.findLinkText(linkText));
	}

	@Then("^I should see a \"([^\"]*)\" partial link$")
	public void i_should_see_a_partial_link(String link) throws Throwable {
		Assert.assertTrue(page.findPartialLinkText(link));
	}

	@When("^I click on the link \"([^\"]*)\"$")
	public void iClickOnTheLink(String linkText) throws Throwable {
		page.clickLink(linkText);
	}

	@When("^I click on the partial link \"([^\"]*)\"$")
	public void iClickOnThePartialLink(String linkText) throws Throwable {
		page.clickPartialLink(linkText);

	}

	@Then("^I should see a \"([^\"]*)\" link$")
	public void i_should_see_a_link(String linkName) throws Throwable {
		Assert.assertTrue(page.findLinkText(linkName));

	}

	@When("^I click on \"([^\"]*)\" link$")
	public void i_click_on_link(String linkName) throws Throwable {
		page.clickLink(linkName);
	}

	@Then("^I should see a \"([^\"]*)\" checkbox with \"([^\"]*)\" icon$")
	public void i_should_see_a_checkbox_with_icon(String text, String id) throws Throwable {
		Assert.assertTrue("Failed to find element with id [" + id + "]", page.verifyElementById(id));
		Assert.assertTrue("Failed to find checkbox with [" + text + "]", page.verifyTextByClassName("checkbox", text));

		// Assert.assertTrue(getElementByIdAndText(id, text));
	}

	@Then("^I check the \"([^\"]*)\" icon$")
	public void i_check_the_icon(String iconId) throws Throwable {
		page.clickElementById(iconId);
	}

	@Then("^I should see the \"([^\"]*)\" button$")
	public void i_should_see_the_button(String buttonName) throws Throwable {
		Assert.assertTrue(page.verifyButtonByText(buttonName));
	}

	@When("^I click on the \"([^\"]*)\" button$")
	public void i_click_on_the_button(String buttonName) throws Throwable {
		page.clickButtonByText(buttonName);
	}

	@When("^I click on \"([^\"]*)\" radio button$")
	public void i_click_on_radio_button(String radioButtonId) throws Throwable {
		page.clickElementById(radioButtonId);
	}

	@Then("^I should see a \"([^\"]*)\" security questions panel$")
	public void i_should_see_a_panel(String panelId) throws Throwable {
		page.verifyElementById(panelId);

	}

	@Then("^I should see \"([^\"]*)\" link enabled$")
	public void i_should_see_link_enabled(String link) throws Throwable {
		Assert.assertTrue("link is not displyed or enabled", page.verifyLinkEnabled(link));
	}

	@Given("^test is running for \"([^\"]*)\"$")
	public void setuptheportalname(String arg1) {

		dataStorage.setPortalName(arg1);

	}

	@Given("^I directly navigate to the root with partial \"([^\"]*)\"$")
	public void iDirectlyNavigateToTheRootWithPartial(String url) throws Throwable {
		String rootUrl = readXMLdata.getTestData("Urls", "HsidRootUrl");
		page.getUrl(rootUrl + url);
	}

	@Then("^I delete the browser cookies$")
	public void I_delete_the_browser_cookies() throws Throwable {
		page.deleteCookies();
	}

	@Then("^I should see a modal dialog$")
	public void i_should_see_a_modal_dialog() throws Throwable {
		Assert.assertTrue(page.verifyElementByClassName("modal__dialog"));
	}

	@Then("^I should see the modal heading \"([^\"]*)\"$")
	public void i_should_see_the_modal_heading(String arg1) throws Throwable {
		Thread.sleep(2000);
		Assert.assertTrue("\"" + arg1 + "\"" + " heading is not displaying", page.verifytheSubHeadingByH2Tag(arg1));
	}

	@Then("^I scroll the page up$")
	public void I_should_see_the_Link() throws Throwable {
		page.scrollthePageUp();
	}

	@Then("^I should see a label with \"([^\"]*)\" heading and \"([^\"]*)\" text box$")
	public void i_should_see_a_label_with_heading_and_text_box(String fieldHeading, String textboxId) throws Throwable {
		Assert.assertTrue(page.verifyFieldNameAndAssociatedTextBox(fieldHeading, textboxId));
	}

}
