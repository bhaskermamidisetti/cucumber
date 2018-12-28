package com.org.ui.base;

import com.org.ui.utility.Hooks;
import com.org.ui.utility.dataStorage;
import com.org.ui.utility.readXMLdata;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SeleniumUtils {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);

    private static List<String> pathList = new ArrayList<>();
    private static String json;
    private static Map<String, String> map = new HashMap<>();

    // Timeouts for each WebDriverWait object, in seconds.
    public static final int SMALL_WAIT_TIME = 5;
    public static final int MEDIUM_WAIT_TIME = 30;
    public static final int LONG_WAIT_TIME = 60;
    public static ThreadLocal<WebDriverWait> smallWait = new ThreadLocal<>();
    public static ThreadLocal<WebDriverWait> mediumWait = new ThreadLocal<>();
    public static ThreadLocal<WebDriverWait> longWait = new ThreadLocal<>();

    public static final String BUTTON_STRING = "button";

    protected static final String BROWSER_CONFIG_FILE_RELATIVE_PATH = "src/main/resources/features/config/browser_config.json";
    protected static final String SYSTEM_CONFIG_FILE_RELATIVE_PATH = "src/main/resources/features/config/system_config.json";
    protected static final String ENVIRONMENT_CONFIG_FILE_RELATIVE_PATH = "src/main/resources/features/config/environment_config.json";

    public WebDriver driver = null;

    public SeleniumUtils() {

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

    public void waitForPageLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = driver1 -> {
            try {
                return ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
            } catch (UnsupportedCommandException e) {
                return false;
            }
        };
        WebDriverWait ninetySecond = new WebDriverWait(driver, 90);

        // longWait.get().until(pageLoadCondition);
        ninetySecond.until(pageLoadCondition);
    }

    public void waitForJavascriptToLoad(int maxWaitMillis, int pollDelimiter) {
        waitForPageLoad(driver);
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + maxWaitMillis) {
            try {
                String prevState = driver.getPageSource();
                Thread.sleep(pollDelimiter);
                if (prevState.equals(driver.getPageSource())) {
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getSystemVariable(String variableName) {
        String requiredValue = "";
        StringBuilder stringBuilder = new StringBuilder("");
        try (FileInputStream fileInputStream = new FileInputStream(SYSTEM_CONFIG_FILE_RELATIVE_PATH)) {

            int i;
            while ((i = fileInputStream.read()) != -1) {
                stringBuilder.append((char) i);
            }
            json = stringBuilder.toString();

            JSONObject object = new JSONObject(json);
            String jsonPath = "";
            readObject(object, jsonPath);
            requiredValue = map.get(variableName);
        } catch (Exception e) {
        }
        return requiredValue;

    }

    public static String getEnvVariable(String jsonVariablePath) {
        String requiredValue = "";
        StringBuilder stringBuilder = new StringBuilder("");
        try (FileInputStream fileInputStream = new FileInputStream(
                "src/main/resources/features/config/" + getSystemVariable("portal_name") + "_environment_config.json");) {
            int i;
            while ((i = fileInputStream.read()) != -1) {
                stringBuilder.append((char) i);
            }
            json = stringBuilder.toString();

            requiredValue = getEnvironmentProperty(jsonVariablePath);
        } catch (Exception e) {
        }
        return requiredValue;

    }

    public static String getEnvironmentProperty(String propertyPath)
            throws FileNotFoundException, IOException, ParseException {
        JSONObject object = new JSONObject(json);
        String jsonPath = "";
        readObject(object, jsonPath);
        return map.get(getSystemVariable("environment.type") + "." + propertyPath);
    }

    private static void readObject(JSONObject object, String jsonPath) {
        Iterator<String> keysItr = object.keys();
        String parentPath = jsonPath;
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if (parentPath != "")
                jsonPath = parentPath + "." + key;
            else if (parentPath == "")
                jsonPath = parentPath + key;

            if (value instanceof JSONArray) {
                readArray((JSONArray) value, jsonPath);
            } else if (value instanceof JSONObject) {
                readObject((JSONObject) value, jsonPath);
            } else {
                pathList.add(jsonPath);
                map.put(jsonPath, (String) value);
            }
        }
    }

    private static void readArray(JSONArray array, String jsonPath) {
        String parentPath = jsonPath;
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            jsonPath = parentPath + "[" + i + "]";

            if (value instanceof JSONArray) {
                readArray((JSONArray) value, jsonPath);
            } else if (value instanceof JSONObject) {
                readObject((JSONObject) value, jsonPath);
            } else {
                pathList.add(jsonPath);
                map.put(jsonPath, (String) value);
            }
        }
    }

    public void scrollElementIntoView(WebElement elem) {
        // OLD SCRIPT: arguments[0].scrollIntoView();
        // NEW SCRIPT: window.scrollBy(0, arguments[0].getBoundingClientRect().top -
        // (window.innerHeight>>1));
        String scrollScript = "window.scrollBy(0, arguments[0].getBoundingClientRect().top - (window.innerHeight/2));";
        ((JavascriptExecutor) driver).executeScript(scrollScript, elem);
    }

    public void clickTab() {
        // WebElement currentElement = driver.switchTo().activeElement();
        WebElement currentElement = driver.findElement(By.tagName("body"));
        currentElement.sendKeys(Keys.TAB);

    }

    public void clickLink(String linkName) {
        WebElement linkElem = longWait.get().until(ExpectedConditions.elementToBeClickable(By.linkText(linkName)));
        scrollElementIntoView(linkElem);
        linkElem.click();

    }

    public void clickPartialLink(String partialLinkName) {
        WebElement linkElem = mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(partialLinkName)));
        scrollElementIntoView(linkElem);
        linkElem.click();
    }

    public void clickElement(WebElement element) {
        scrollElementIntoView(element);
        element.click();

    }

    public void getUrl(String pageUrl) {

        driver.get(pageUrl);
        waitForPageLoad(driver);

    }

    public void openNewTab() {
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        driver.switchTo().defaultContent();
    }

    public void switchBackToPreviousTab() {
        Actions action = new Actions(driver);
        action.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).sendKeys(Keys.TAB).build().perform();
        driver.switchTo().defaultContent();
    }

    public void switchToNextTab() {
        Actions action = new Actions(driver);
        action.keyDown(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
        driver.switchTo().defaultContent();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void clickBrowserBackButton() {
        driver.navigate().back();
    }

    public void openBrowser(String url) {
        driver.get(url);
        waitForPageLoad(driver);
    }

    public boolean findLinkText(String expectedLinkText) {
        WebElement elem = mediumWait.get()
                .until(ExpectedConditions.presenceOfElementLocated(By.linkText(expectedLinkText)));
        return elem != null;

    }

    public boolean findPartialLinkText(String expectedLinkText) {
        WebElement elem = mediumWait.get()
                .until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(expectedLinkText)));
        return elem != null;

    }

    public boolean verifyPageTitle(String expectedPageTitle) {
        String actualPageHeading = mediumWait.get()
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("title"))).getText();
        return actualPageHeading.trim().equals(expectedPageTitle);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void enterValueById(String id, String value) {
        mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.id(id))).sendKeys(value);
    }

    public boolean verifyElementByClassName(String className) {
        WebElement elem = mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.className(className)));
        return elem != null;
    }

    public boolean getElementByIdAndText(String id, String text) {
        WebElement elem = mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        return elem.getText().trim().equals(text);
    }

    public boolean verifyElementByName(String name) {
        WebElement elem = mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
        return elem != null;

    }

    public boolean verifyTextByClassName(String className, String text) {
        boolean isverified = false;
        try {
            List<WebElement> elems = mediumWait.get()
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(className)));
            for (WebElement elem : elems) {
                if (elem.getText().contains(text)) {
                    isverified = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isverified = false;
        }
        return isverified;

    }

    public boolean verifyElementById(String id) {
        WebElement elem = longWait.get().until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        return elem != null;
    }

    public boolean clickButtonByTypeAndTitle(String buttonTitle) {
        List<WebElement> elems = smallWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(BUTTON_STRING)));
        for (WebElement elem : elems) {
            if (elem.getAttribute("type").equals(BUTTON_STRING) && elem.getAttribute("title").equals(buttonTitle)) {
                elem.click();
                return true;
            } else {
                break;
            }
        }
        return false;
    }

    public void clickElementById(String id) {
        WebElement elem = smallWait.get().until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        scrollElementIntoView(elem);
        elem.click();
    }

    public boolean verifyButtonBySubmitTypeAndName(String buttonName) {
        WebElement elem = smallWait.get().until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//button[@type='submit' and contains(.,'" + buttonName + "')]")));
        return elem != null;
    }

    public void clickButtonBySubmitTypeAndName(String buttonName) {
        WebElement elem = smallWait.get().until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//button[@type='submit' and contains(.,'" + buttonName + "')]")));
        scrollElementIntoView(elem);
        elem.click();
    }

    public boolean verifyButtonByText(String buttonName) {
        WebElement elem = smallWait.get().until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(.,'" + buttonName + "')]")));
        return elem != null;
    }

    public void clickButtonByText(String buttonName) {
        WebElement elem = smallWait.get().until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(.,'" + buttonName + "')]")));
        scrollElementIntoView(elem);
        elem.click();
    }

    public boolean clickOnALinkByhrefPartialText(String url) {

        boolean clicked = false;
        List<WebElement> elems = mediumWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("a")));
        for (WebElement elem : elems) {
            if (elem.getAttribute("href").contains(url)) {
                scrollElementIntoView(elem);
                elem.click();
                refreshPage();
                clicked = true;
            }
        }
        return clicked;

    }

    public boolean clickElementByClassNameAndText(String className, String text) {

        boolean isClicked = false;
        try {
            List<WebElement> elems = new WebDriverWait(driver, 30)
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(className)));
            for (WebElement elem : elems) {
                if (elem.getText().contains(text)) {
                    elem.click();
                    isClicked = true;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
        }
        return isClicked;
    }

    public boolean selectDropdownItemFromList(String selectDropdownId, String dropdownText) {
        boolean isSelected = false;
        Select dropdown = new Select(
                mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.id(selectDropdownId))));
        dropdown.selectByVisibleText(dropdownText);
        isSelected = true;
        return isSelected;

    }

    public void handleSecurityQuestionWithAnswer() throws Exception {

        String SecurityQtn = mediumWait.get()
                .until(ExpectedConditions.presenceOfElementLocated(By.id("authQuestiontextLabelId"))).getText();
        WebElement answerField = mediumWait.get()
                .until(ExpectedConditions.presenceOfElementLocated(By.id("challengeQuestionList[0].userAnswer")));
        answerField.clear();
        // Small pause to resolve intermittent clearing of input field from page update
        Thread.sleep(1000);
        if (SecurityQtn.contains("number")) {
            answerField.sendKeys("number1");
        } else if (SecurityQtn.contains("name")) {
            answerField.sendKeys("name1");
        } else if (SecurityQtn.contains("color")) {
            answerField.sendKeys("color1");
        } else {
            throw new Exception("unknown challenge " + SecurityQtn);
        }
        clickTab();

        // mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.id("remembermydevice"))).click();
        mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.id("continueSubmitButton"))).click();
        // return
        // mediumWait.get().until(ExpectedConditions.visibilityOfElementLocated(By.id("usermenu"))).isDisplayed();

    }

    public void switchToFrameByNameOrId(String frameNameOrId) {
        driver.switchTo().frame(frameNameOrId);

    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();

    }

    public String getCurrentPageUrl() {
        waitForPageLoad(driver);
        return driver.getCurrentUrl();

    }

    public String getPagelinkFromEnvConfigFile(String pageName, String linkName)
            throws IOException, InterruptedException {
        Thread.sleep(10000);
        return getEnvVariable("pageLinks." + pageName + "." + linkName);
    }

    public String getPagelinkFromTestDataXMLFile(String pageName, String linkName)
            throws IOException, InterruptedException {

        return readXMLdata.getTestData(dataStorage.getPortalName() + "/Link",
                pageName.replace(" ", "") + "." + linkName.replace(" ", "")).trim();

    }

    public boolean VerifyButtonByTagNameAndText(String buttonName) {
        WebElement reqElement = null;
        List<WebElement> elems = mediumWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(BUTTON_STRING)));
        for (WebElement elem : elems) {
            if (elem.getText().equals(buttonName)) {
                reqElement = elem;
                break;
            }
        }
        return reqElement != null;
    }

    public WebElement getAnchorElementByText(String anchorText) {
        WebElement webElement = null;
        List<WebElement> elems = mediumWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("a")));
        for (WebElement elem : elems) {
            if (elem.getText().equals(anchorText)) {
                webElement = elem;
                break;
            }
        }
        return webElement;
    }

    public boolean verifyLinkEnabled(String linkText) {
        return mediumWait.get().until(ExpectedConditions.presenceOfElementLocated(By.linkText(linkText))).isEnabled();
    }

    public void closeCurrentWindowOfheBrowser() {
        driver.close();
    }

    public void scrollthePageUp() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0, -250);");
    }

    public void deleteCookies() {
        driver.manage().deleteAllCookies();
    }

    public boolean verifythePageHeadingByH1Tag(String expectedSubHeading) {
        List<WebElement> subHeadings = mediumWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("h1")));
        String pageHeading = null;
        for (WebElement actualPageHeading : subHeadings) {
            if (actualPageHeading.getText().contains(expectedSubHeading)) {
                pageHeading = actualPageHeading.getText();
                break;
            }

        }
        return pageHeading != null;
    }

    public boolean verifytheSubHeadingByH2Tag(String expectedSubHeading) {
        List<WebElement> subHeadings = mediumWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("h2")));
        String h2subHeading = null;
        for (WebElement actualSubHeading : subHeadings) {
            if (actualSubHeading.getText().contains(expectedSubHeading)) {
                h2subHeading = actualSubHeading.getText();
            }

        }
        return h2subHeading != null;
    }

    public boolean verifytheSubHeadingByH3Tag(String expectedSubHeading) {
        List<WebElement> subHeadings = mediumWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("h3")));
        String h3subHeading = null;
        for (WebElement actualSubHeading : subHeadings) {
            if (actualSubHeading.getText().contains(expectedSubHeading)) {
                h3subHeading = actualSubHeading.getText();
            }

        }
        return h3subHeading != null;
    }

    public boolean verifytheSubHeadingByPTag(String expectedSubHeading) {
        // Wait for <p> element containing expected SubHeading text
        By selector = By.xpath("//p[contains(.,'" + expectedSubHeading + "')]");
        try {
            List<WebElement> subHeadings = mediumWait.get()
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
            for (WebElement actualSubHeading : subHeadings) {
                if (actualSubHeading.isDisplayed()) {
                    return true;
                }
            }
            // None of the found elements are displayed, return false
            return false;
        } catch (TimeoutException e) {
            return false;
        }

    }

    public boolean verifytheSubHeadingByspan(String expectedSubHeading) {
        List<WebElement> subHeadings = mediumWait.get()
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("span")));
        String h2subHeading = null;
        for (WebElement actualSubHeading : subHeadings) {
            if (actualSubHeading.getText().contains(expectedSubHeading)) {
                h2subHeading = actualSubHeading.getText();
            }

        }
        return h2subHeading != null;
    }

    /**
     * Given a WebElement, scroll that element into view and execute a mouseOver on
     * it
     *
     * @param elem
     */
    protected void mouseHoverOnElement(WebElement elem) {

        // perform click to dismiss any current tooltip
        // driver.findElement(By.xpath("//body")).click();

        scrollElementIntoView(elem);

        /*
         * FF52 on SauceLabs does not support the moveToElement() action method Actions
         * action = new Actions(driver); action.moveToElement( mediumWait.get()
         * .until(ExpectedConditions.presenceOfElementLocated(
         * By.xpath("//*[@class='tooltip']/*[contains(.,'" + message + "')]/i"))))
         * .build().perform();
         *
         */

        // COPIED FROM: http://codoid.com/webdriver-mouseover/
        // The below JavaScript code creates, initializes and dispatches mouse event to
        // an object on fly.
        String strJavaScript = "var mouseEventObj = document.createEvent('MouseEvents');"
                + "mouseEventObj.initEvent( 'mouseover', true, true );" + "arguments[0].dispatchEvent(mouseEventObj);";

        // Then JavascriptExecutor class is used to execute the script to trigger the
        // dispatched event.
        ((JavascriptExecutor) driver).executeScript(strJavaScript, elem);
    }

    public boolean verifyFieldNameAndAssociatedTextBox(String spanTagText, String followingSiblingInputId) {

        WebElement elem = mediumWait.get().until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@class,'strong') and contains(.,'"
                        + spanTagText + "')]/following-sibling::input[@id='" + followingSiblingInputId + "']")));

        return elem != null;
    }

}
