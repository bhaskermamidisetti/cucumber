package com.org.ui.utility;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * This class deals with selenium utility.
 * <p>
 */
public class SeleniumUtilsTestNG {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumUtilsTestNG.class);

    public int maxRetry;

    public int explicitWait;

    public int pageRefreshWait;
    public static WebDriver driver;
    public int waitInMilliSec;
    public int refreshWaitInMilliSec;
    public int alertWaitInMilliSec;

    public SeleniumUtilsTestNG(Properties properties) {
        // maxRetry =
        // Integer.parseInt(properties.getProperty(PropertyConstants.MAX_RETRY));
        maxRetry = Integer.parseInt(properties.getProperty("maxRetry"));
        explicitWait = Integer.parseInt(properties.getProperty("10")); // PropertyConstants.EXPLICIT_WAIT
        pageRefreshWait = Integer.parseInt(properties.getProperty("10")); // PropertyConstants.PAGE_REFRESH_WAIT
        waitInMilliSec = Integer.parseInt(properties.getProperty("10")); // PropertyConstants.WAIT_IN_MILLI_SEC
        alertWaitInMilliSec = Integer.parseInt(properties.getProperty("10")); // PropertyConstants.ALERT_WAIT_IN_MILLI_SEC
        refreshWaitInMilliSec = Integer.parseInt(properties.getProperty("10")); // PropertyConstants.REFRESH_WAIT_IN_MILLI_SEC
    }

    /**
     * Find element with given parameter.
     */
    public WebElement findElement(WebDriver driver, Identifier identifier, String locator) {
        By by = getByLocator(identifier, locator);
        return driver.findElement(by);
    }

    /**
     * Find all element with given parameter.
     */
    public List<WebElement> findElements(WebDriver driver, Identifier identifier, String locator) {
        By by = getByLocator(identifier, locator);
        return driver.findElements(by);
    }

    public By getByLocator(Identifier identifier, String locator) {
        By by = null;
        switch (identifier) {
            case ID:
                by = By.id(locator);
                break;
            case NAME:
                by = By.name(locator);
                break;
            case CLASS_NAME:
                by = By.className(locator);
                break;
            case CSS:
                by = By.cssSelector(locator);
                break;
            case XPATH:
                by = By.xpath(locator);
                break;
        }
        return by;
    }

    /**
     * Send text key to webelement. Clear the text before sending the key.
     */
    public void sendKeys(WebElement webElement, String text) {
        webElement.clear();
        waitElementValueTextEmpty(webElement, pageRefreshWait, TimeUnit.SECONDS);
        webElement.sendKeys(text);
    }

    public boolean waitForElementDisplay(WebDriver driver, WebElement webElement) {
        return waitForElementDisplay(driver, webElement, explicitWait);
    }

    /**
     * Waits until given element is displayed.
     */
    public boolean waitForElementDisplay(WebDriver driver, WebElement webElement, int timeOut) {
        try {
            Wait<WebElement> wait = new FluentWait<WebElement>(webElement).withTimeout(timeOut, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(new Function<WebElement, Boolean>() {
                public Boolean apply(WebElement element) {
                    return element.isDisplayed();
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits until given element is displayed.
     */
    public boolean waitForElementDisplay(WebDriver driver, Identifier identifier, String locator) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(explicitWait, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return findElement(driver, identifier, locator).isDisplayed();
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits till text in the element is not empty then returns the test.
     */
    public String getElementTextDisplay(WebDriver driver, Identifier identifier, String locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(explicitWait, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                String text = findElement(driver, identifier, locator).getText();
                return null != text && !text.isEmpty();
            }
        });
        return findElement(driver, identifier, locator).getText();
    }

    /**
     * Waits till text in the element is not empty then returns the test.
     */
    public boolean waitElementValueTextDisplay(WebElement element) {
        try {
            new FluentWait<WebElement>(element).withTimeout(explicitWait, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class)
                    .until(new Function<WebElement, Boolean>() {
                        @Override
                        public Boolean apply(WebElement element) {
                            String text = element.getAttribute("value"); // "value"
                            return null != text && !text.isEmpty();
                        }
                    });
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits till text in the element is empty then returns the test.
     */
    public boolean waitElementValueTextEmpty(WebElement element, int timeOut, TimeUnit timeUnit) {
        try {
            new FluentWait<WebElement>(element).withTimeout(timeOut, timeUnit).ignoring(NoSuchElementException.class)
                    .ignoring(WebDriverException.class).until(new Function<WebElement, Boolean>() {
                @Override
                public Boolean apply(WebElement element) {
                    String text = element.getAttribute("value"); // "value"
                    return null == text || text.isEmpty();
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits till given element is clickable.
     */
    public boolean waitForElementClickable(WebDriver driver, WebElement webElement) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(explicitWait, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits till given element is clickable.
     */
    public boolean waitForElementClickable(WebDriver driver, Identifier identifier, String locator) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(explicitWait, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.elementToBeClickable(getByLocator(identifier, locator)));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Waits until page is loaded.
     */
    public void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, explicitWait);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        });
    }

    /**
     * Waits until only one element is found for the given locator.
     */
    public boolean waitForSingleElementVisibility(WebDriver driver, Identifier identifier, String locator) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(pageRefreshWait, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    List<WebElement> results = findElements(driver, identifier, locator);
                    return null != results && !results.isEmpty() && results.size() == 1;
                }
            });
        } catch (Exception e) {
            // TestBase.debug("Exception while waiting for display of element", e);
            return false;
        }
        return true;
    }

    /**
     * Takes screenshot of the applciation.
     *
     * @return screenshot location.
     */
    /*
     * public String takeScreenshot(WebDriver driver) { TakesScreenshot screenshot =
     * (TakesScreenshot) driver; File source =
     * screenshot.getScreenshotAs(OutputType.FILE); File dest = new File(
     * Constants.SCREENSHOT_DIR + "Screenshot_" + Commons.getCurrentTimeStamp() +
     * ".png"); try { org.apache.commons.io.FileUtils.copyFile(source, dest); }
     * catch (IOException e) { //
     * TestBase.debug("IOException occurred while saving screenshot", e); } String
     * path = dest.getAbsolutePath(); logger.info("Screen shot path : " + path);
     * return dest.getAbsolutePath(); }
     */
    public boolean waitFor(WebDriver driver, int timeout, TimeUnit timeUnit) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeout, timeUnit)
                    .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return !((JavascriptExecutor) driver).executeScript("return document.readyState")
                            .equals("complete");
                }
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Waits till page refresh is triggered.
     */
    public void waitForPageRefresh(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, pageRefreshWait);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return !((JavascriptExecutor) driver).executeScript("return document.readyState")
                            .equals("complete");
                }
            });
        } catch (TimeoutException e) {
        }
    }

    /**
     * Clicks on a Button waiting until that it is clickable.
     */
    public boolean waitClickElement(WebDriver driver, Identifier identifier, String locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(explicitWait, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class)
                .ignoring(ElementNotVisibleException.class);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                findElement(driver, identifier, locator).click();
                return true;
            }
        });
        return true;
    }

    /**
     * Clicks on a Button waiting until that it is clickable.
     */
    public boolean waitClickElement(WebElement webElement) {
        new FluentWait<WebElement>(webElement).withTimeout(explicitWait, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class).ignoring(WebDriverException.class)
                .ignoring(ElementNotVisibleException.class).ignoring(TimeoutException.class)
                .until(new Function<WebElement, Boolean>() {
                    @Override
                    public Boolean apply(WebElement element) {
                        element.click();
                        return true;
                    }
                });
        return true;
    }

    public void implicitlyWait(int timeOut, TimeUnit timeUnit) {
        driver.manage().timeouts().implicitlyWait(timeOut, timeUnit);
    }

    public void scrollIntoView(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollUp() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0, -250);");
    }

    public void scrollDown() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0, 250);");
    }

    public void moveToElementClick(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }

    public void moveToElement(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public void selectByValueFromDropDown(WebElement element, String value) {
        Select selectFromDropDown = new Select(element);
        selectFromDropDown.selectByValue(value);
    }

    public void selectByVisibleTextFromDropDown(WebElement element, String value) {
        Select selectFromDropDown = new Select(element);
        selectFromDropDown.selectByVisibleText(value);
    }

    public void selectByIndexFromDropDown(WebElement element, int index) {
        Select selectFromDropDown = new Select(element);
        selectFromDropDown.selectByIndex(index);
    }

    public boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void zoomOut(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='80%'");
    }

    public void zoomIn(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='100%'");
    }

    public enum Identifier {
        ID, NAME, CLASS_NAME, CSS, XPATH
    }

}
