package com.org.ui.pages;

import com.org.ui.base.SeleniumUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NewTourseDemoPage extends SeleniumUtils {

    @FindBy(xpath = "//input[@name=\"userName\"]")
    private WebElement userId;

    @FindBy(xpath = "//input[@name=\"password\"]")
    private WebElement password;

    @FindBy(xpath = "//input[@name=\"login\"]")
    private WebElement loginButton;

    @FindBy(xpath = "//a[text()='SIGN-OFF']")
    private WebElement logoutButton;

    public void enterUserNamePassword(String arg1, String arg2) {

        userId.sendKeys(arg1);
        password.sendKeys(arg2);
    }

    public void clickOnLogin() {
        loginButton.click();
    }

    public boolean VerifySignOffLinkDisplayed() {
        return longWait.get().until(ExpectedConditions.visibilityOf(logoutButton)).isDisplayed();
    }

}
