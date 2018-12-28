package com.org.ui.base;

import com.org.ui.utility.Hooks;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class TestBase {

    public WebDriver driver = null;

    public TestBase() {

        try {
            if (driver == null) {
                driver = Hooks.createAndGetDeviceDriver();
                PageFactory.initElements(driver, this);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Assert.fail("Exception occurred instantiating SeleniumUtilsTestNG.driver [" + e.getMessage() + "]");
        }

    }
}
