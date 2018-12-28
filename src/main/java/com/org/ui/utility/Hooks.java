package com.org.ui.utility;

import com.org.ui.base.SeleniumUtils;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.deps.com.thoughtworks.xstream.InitializationException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final static String FIREFOX_BROWSER = "FIREFOX";
    private final static String DEFAULT_BROWSERTYPE = FIREFOX_BROWSER;
    private final static String DEFAULT_BROWSERVERSION_SAUCE = "57";
    private final static String DEFAULT_BROWSERVERSION_LOCAL = "52";
    private final static String DEFAULT_BROWSERENV = "local";
    private final static String DEFAULT_SAUCE_TUNNEL = "TunnelNameHere";
    private final static String DEFAULT_SAUCE_USER = "usernameHere";
    private final static String DEFAULT_SAUCE_ACCESSKEY = "AccessKeyHere";
    private final static String WINDOWS_7_CONSTANT = "Windows 7";
    private final static String PLATFORM_CONSTANT = "platform";


    @Before
    public void beforehook(Scenario scenario) {

        Collection<String> Tags = scenario.getSourceTagNames();
        // dataStorage.setScenarioName(scn.getId().substring(0, 8).toUpperCase() + "-" + scn.getName().replace(" ", "_"));

        String tagsclcnton = Tags.toString();
        // System.out.println("I am in before hook with tag collection [" + tagsclcnton + "]");

        if (tagsclcnton.toUpperCase().contains("@UI")) {
            dataStorage.setPortalName("UI");
        } else if (tagsclcnton.toUpperCase().contains("@DEMO")) {
            dataStorage.setPortalName("DEMO");
        }

    }

    @After
    public void afterhook(Scenario scenario) throws WebDriverException, IOException, SQLException {

        // Wrap in try/finally block in case something is broken with driver object
        // Want to ensure that is closed and nulled out for future use.
        try {
            // scn.write(dataStorage.getCustomErrmsg());
            final byte[] screenshot = ((TakesScreenshot) getDeviceDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png"); // ... and embed it in

        } finally {
            // Reset dataStorage values for next test using this thread
            // and return any ResourcePool data
            dataStorage.resetAllDataStorage();
            closeDeviceDriver();
            //MysqlConnection.getDBConnection().close();

        }
    }


    /**
     * Method to return driver instance for current thread. If one is not set,
     * create a new driver from system property configuration.
     *
     * @return Current thread's driver instance or newly created driver
     * @throws MalformedURLException If invalid SauceLabs URL is built
     */
    public static WebDriver createAndGetDeviceDriver() throws MalformedURLException {

        if (driver.get() != null) {
            return driver.get();
        }

        // Retrieve desired browser configuration from system properties
        String BrowserVersion = System.getProperty("BrowserVersion");
        String BrowserType = System.getProperty("BrowserType");
        String browserEnv = System.getProperty("BrowserEnv");
        String sauceUsername = System.getenv("SAUCE_USERNAME");
        String saucePassword = System.getenv("SAUCE_ACCESS_KEY");
        String sauceTunnel = System.getenv("SAUCE_TUNNEL_NAME");

        if (browserEnv == null) {
            browserEnv = DEFAULT_BROWSERENV;
        }

        if (BrowserVersion == null && browserEnv.equals("saucelab")) {
            BrowserVersion = DEFAULT_BROWSERVERSION_SAUCE;
        } else if (BrowserVersion == null) {
            BrowserVersion = DEFAULT_BROWSERVERSION_LOCAL;
        }
        if (BrowserType == null) {
            BrowserType = DEFAULT_BROWSERTYPE;
        }

        if (browserEnv.equalsIgnoreCase("saucelab")) {

            sauceUsername = getSauceUsername(sauceUsername);
            saucePassword = getSaucePassword(saucePassword);
            sauceTunnel = getSauceTunnel(sauceTunnel);

            DesiredCapabilities capabilities;

            switch (BrowserType.toUpperCase()) {

                case "IE":
                    capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability(PLATFORM_CONSTANT, WINDOWS_7_CONSTANT);
                    break;

                case FIREFOX_BROWSER:
                    capabilities = DesiredCapabilities.firefox();
                    capabilities.setCapability(PLATFORM_CONSTANT, WINDOWS_7_CONSTANT);
                    break;

                case "SAFARI":
                    capabilities = DesiredCapabilities.safari();
                    capabilities.setCapability(PLATFORM_CONSTANT, "OS X 10.11");
                    capabilities.setCapability("technologyPreview", true);
                    break;

                case "CHROME":
                    capabilities = DesiredCapabilities.chrome();
                    capabilities.setCapability(PLATFORM_CONSTANT, WINDOWS_7_CONSTANT);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported Platform/Browser Configuration " + BrowserType);

            }

            boolean toRecordLog = !("prod".equalsIgnoreCase(System.getProperty("ExecutionEnv")));
            URL sauceLabUrl = new URL(
                    "http://" + sauceUsername + ":" + saucePassword + "@ondemand.saucelabs.com:80/wd/hub");

            // Capabilities -> capabilities.setCapability(PLATFORM_CONSTANT,
            // WINDOWS_7_CONSTANT);
            capabilities.setCapability("version", BrowserVersion);
            capabilities.setCapability("maxDuration", 2700);
            capabilities.setCapability("avoidProxy", true);
            capabilities.setCapability("autoAcceptAlerts", true);

            capabilities.setCapability("parent-tunnel", "sauce_admin");
            capabilities.setCapability("tunnelIdentifier", sauceTunnel);

            capabilities.setCapability("recordVideo", true);
            capabilities.setCapability("recordScreenshots", toRecordLog);
            capabilities.setCapability("recordLogs", toRecordLog);
            capabilities.setCapability("screenResolution", "1280x768");
            // sauce labs time-outs
            capabilities.setCapability("idleTimeout", 120);
            capabilities.setCapability("commandTimeout", 240);

            capabilities.setCapability("extendedDebugging", true);

            // Capabilities -> capabilities.setCapability("name",
            // dataStorage.getScenarioName());

            setDriver(new RemoteWebDriver(sauceLabUrl, capabilities));
            // dataStorage -> dataStorage.setCustomErrmsg("Play in saucelab: "
            // +"https://saucelabs.com/beta/tests/" + ((RemoteWebDriver)
            // driver.get()).getSessionId().toString());

        } else {
            // Instantiate local browser

            switch (BrowserType.toUpperCase()) {

                case "IE":
                    DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability(CapabilityType.BROWSER_NAME, "internet explorer");
                    capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                            true);
                    InternetExplorerOptions options = new InternetExplorerOptions(capabilities);
                    System.setProperty("webdriver.ie.driver", "src\\main\\resources\\drivers" + File.separator + "IEDriverServer.exe");
                    setDriver(new InternetExplorerDriver(options));
                    break;

                case FIREFOX_BROWSER:
                    DesiredCapabilities firefoxCapabilities = new DesiredCapabilities();
                    boolean isMarionette = true;
                    if (Integer.parseInt(BrowserVersion) < 47) {
                        isMarionette = false;
                    }
                    firefoxCapabilities.setCapability("marionette", isMarionette);
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setCapability("marionette", isMarionette);
                    System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\drivers" + File.separator + "geckodriver.exe");
                    setDriver(new FirefoxDriver(firefoxOptions));
                    break;

                case "CHROME":
                    System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers" + File.separator + "chromedriver.exe");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("disable-infobars"); // disabling the info bar
                    chromeOptions.addArguments("--dns-prefetch-disable");
                    chromeOptions.addArguments("disable-plugins");
                    chromeOptions.addArguments("disable-extensions");
                    chromeOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                    // chromeOptions.addArguments("start-maximized");
                    setDriver(new ChromeDriver(chromeOptions));
                    break;

                case "EMULATION":
                    System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers" + File.separator + "chromedriver.exe");
                    ChromeOptions chromeoptions = new ChromeOptions();
                    chromeoptions.addArguments("disable-infobars"); // disabling the info bar
                    // Chrome options -> chromeOptions.addArguments("start-maximized");
                    chromeoptions.setExperimentalOption("mobileEmulation", getMobileEmulationOptions("iPhone X"));
                    setDriver(new ChromeDriver(chromeoptions));
                    break;

                case "EDGE":
                    System.setProperty("webdriver.edge.driver", "src\\main\\resources\\drivers" + File.separator + "MicrosoftWebDriver.exe");
                    setDriver(new EdgeDriver());
                    break;

                case "HEADLESS FIREFOX":
                    FirefoxBinary firefoxBinary = new FirefoxBinary();
                    firefoxBinary.addCommandLineOptions("--headless");
                    FirefoxOptions firefoxHeadlessOptions = new FirefoxOptions();
                    firefoxHeadlessOptions.setBinary(firefoxBinary);
                    System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\drivers" + File.separator + "geckodriver.exe");
                    setDriver(new FirefoxDriver(firefoxHeadlessOptions));
                    break;

                case "HEADLESS CHROME":
                    ChromeOptions chromeHeadlessOptions = new ChromeOptions();
                    chromeHeadlessOptions.addArguments("headless");
                    System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers" + File.separator + "chromedriver.exe");
                    setDriver(new ChromeDriver(chromeHeadlessOptions));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported Platform/Browser Configuration " + BrowserType);

            }

        }

        driver.get().manage().deleteAllCookies();
        driver.get().manage().window().maximize();

        // Initialize threadLocal instances of wait objects with new driver instance
        SeleniumUtils.smallWait.set(new WebDriverWait(getDeviceDriver(), SeleniumUtils.SMALL_WAIT_TIME));
        SeleniumUtils.mediumWait.set(new WebDriverWait(getDeviceDriver(), SeleniumUtils.MEDIUM_WAIT_TIME));
        SeleniumUtils.longWait.set(new WebDriverWait(getDeviceDriver(), SeleniumUtils.LONG_WAIT_TIME));

        return getDeviceDriver();
    }

    private static String getSauceUsername(String sauceUsername) {
        if (sauceUsername == null) {
            return DEFAULT_SAUCE_USER;
        } else {
            return sauceUsername;
        }
    }

    private static String getSaucePassword(String saucePassword) {
        if (saucePassword == null) {
            return DEFAULT_SAUCE_ACCESSKEY;
        } else {
            return saucePassword;
        }
    }

    private static String getSauceTunnel(String sauceTunnel) {
        if (sauceTunnel == null) {
            return DEFAULT_SAUCE_TUNNEL;
        } else {
            return sauceTunnel;
        }
    }

    private static Map<String, String> getMobileEmulationOptions(String device) {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", device);
        return mobileEmulation;
    }

    /**
     * @return driver object for current execution thread
     * @throws InitializationException If browser has not been set
     */
    public static WebDriver getDeviceDriver() throws InitializationException {

        if (driver.get() != null) {
            return driver.get();
        }

        throw new InitializationException("Browser Driver Not Initialized");
    }

    /**
     * @param inputDriver Driver object to store in ThreadLocal<WebDriver>
     */
    private static void setDriver(WebDriver inputDriver) {
        driver.set(inputDriver);
    }

    /**
     * Closes driver for current thread, sets current thread driver to null.
     *
     * @return true if driver shut down, otherwise false.
     */
    public static boolean closeDeviceDriver() {

        try {
            WebDriver currentDriver = getDeviceDriver();
            if (currentDriver != null) {
                setDriver(null);
                currentDriver.quit();
            }
            return getDeviceDriver() == null;
        } catch (InitializationException e) {
            return true;
        }
    }

}
