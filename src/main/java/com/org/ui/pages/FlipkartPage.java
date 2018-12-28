package com.org.ui.pages;

import com.org.ui.base.SeleniumUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class FlipkartPage extends SeleniumUtils {

    @FindBy(css = "button._2AkmmA._29YdH8")
    private WebElement loginClose;

    @FindBy(xpath = "//input[@name='q']")
    private WebElement searchField;

    @FindBy(css = "path._2BhAHa")
    private WebElement searchButton;

    @FindBy(css = "span._2yAnYN")
    private WebElement sampleText;

    public void clickOnSearchProductField() {
        if (loginClose.isDisplayed()) {
            loginClose.click();
        } else {
            searchField.click();
        }
    }

    public void enterProductName(String productName) {
        searchField.sendKeys(productName);
    }

    public void clickOnSearchIcon() {
        searchButton.click();
    }

    public String verifyTest() {
        System.out.println(sampleText.getText());
        return sampleText.getText();
    }

}
