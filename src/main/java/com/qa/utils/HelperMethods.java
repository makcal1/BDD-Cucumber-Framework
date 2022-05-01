package com.qa.utils;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import com.qa.base.ConfigReader;
import com.qa.TestToolException;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import static com.qa.base.BasePage.*;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


import static com.qa.pages.WaitingPage.waitForSeconds;
import static org.openqa.selenium.support.PageFactory.initElements;

public class HelperMethods<statıc> {
    @FindBy(xpath = "//*[@id='CybotCookiebotDialogBodyButtonAccept']")
    private static WebElement acceptCookies;
    @FindBy(xpath = "//*[contains(@id,'leadinModal')]/div[2]/button")
    private static WebElement closeTabOnCookies;
    public Actions actions = new Actions(driver);
    FluentWait<WebDriver> wait = new FluentWait<>(driver);

    /**
     * Create Public and Useful Methods in order to use in "Pages Package"
     */
    public HelperMethods() {initElements(driver, this);}
    public HelperMethods and() {return this;}

    /**
     * General Selenium Wait that can be modified in Constants by changing the "EXPLICIT_WAIT_TIME"
     *
     * @return - web element
     */
    public static WebDriverWait getWaitObject() {return new WebDriverWait(driver, Constants.EXPLICIT_WAIT_TIME);}

    /**
     * Get Element and use flash and draw if it's selected as 'yes' in the configuration.properties file
     *
     * @param locator - web element locator
     * @return - locator
     */
    public static WebElement getElement(By locator) {
        WebElement element = null;

        try {
            JavaScriptUtil.helperMethods.waitForVisibility(driver.findElement(locator));
            element = driver.findElement(locator);
            if (highlightElement) {
                JavaScriptUtil.flash(element);
            }
            if (drowBorderOnElement) {
                JavaScriptUtil.drawBorder(element);
            }

        } catch (NoSuchElementException e) {
            throw new TestToolException("Some exception got occurred while getting the web element: " + element + ": " + e.getCause());
        }

        return element;
    }

    /**
     * Get Element and use flash and draw if it's selected as 'yes' in the configuration.properties file
     *
     * @return - locator
     */
    public static By getXpath(String webELementName) {return By.xpath(webELementName);}

    /**
     * Get Page Title
     */
    public static String doGetPageTitle() {
        try {
            return driver.getTitle();
        } catch (NoSuchContextException e) {
            throw new TestToolException("Some exception occurred while getting the page title: " + e.getCause());

        }
    }

    /**
     * Navigate to the page and wait till the page is loaded to avoid potential exception error
     */

    public static void navigateToPage(String URL) {
        try {
            driver.navigate().to(URL);
            driver.manage().timeouts().pageLoadTimeout(Constants.PAGE_LOAD_WAIT_TIME, TimeUnit.MILLISECONDS);
        } catch (NoSuchContextException e) {
            throw new TestToolException("Some exception occurred while navigating the the page: " + driver.getCurrentUrl() + ": " + e.getCause());

        }
    }
    /**
     * Get text from the element
     * @param element - web element
     * @return - element
     */
    public static String doGetText(WebElement element) {
        try {
            if (highlightElement) {
                JavaScriptUtil.flash(element);
            }
            if (drowBorderOnElement) {
                JavaScriptUtil.drawBorder(element);
            }
            return element.getText();
        } catch (NoSuchElementException e) {
            throw new TestToolException("Some exception occurred while getting the text from the element: " + element + ": " + e.getCause());

        }
    }
    /**
     * Find the duplicate names in the list
     * @param list - list name
     */
    public static void findAndPrintDuplicateNamesInTheList(List<String> list) {
        Map<String, Long> frequencies = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("<----------------------------------------------------------------------------------------------------------------------->");
        //   filter only the inputs which have frequency equal to 2
        frequencies.entrySet().stream().filter(entry -> entry.getValue() == 2).forEach(entry -> System.out.println("Duplicate Names: " + Collections.singletonList(entry.getKey())));
        System.out.println("<----------------------------------------------------------------------------------------------------------------------->");
    }
    /**
     * Find the triplicate names in the list
     * @param list - list name
     */
    public static void findAndPrintTriplicateNamesInTheList(List<String> list) {
        Map<String, Long> frequencies = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        //filter only the inputs which have frequency equal to 3
        frequencies.entrySet().stream().filter(entry -> entry.getValue() == 3).forEach(entry -> System.out.println("Triplicate Names: " + Collections.singletonList(entry.getKey())));
        System.out.println("<----------------------------------------------------------------------------------------------------------------------->");
    }
    /**
     * Print "System.out.println" in a shorter way
     * @param line - line that will be printed
     */
    public HelperMethods printInfo(Object line) {System.out.println(line);return this;}

    /**
     * Print system.info in a shorter way to define that the functional operation is started!
     * @param line - line that will be printed
     */
    public HelperMethods printInfoMethodStarted(Object line) {System.out.println(line + "start!");return this;}

    /**
     * Print system.info in a shorter way to declare that the functional operation is ended!
     * @param line - line that will be printed
     */
    public HelperMethods printInfoMethodEnded(Object line) {System.out.println(line + "end!");return this;}

    /**
     * Wait until the element is presented
     * @param element - web element
     */
    public HelperMethods waitForVisibility(WebElement element) {
        try {
            final FluentWait<WebDriver> webDriverFluentWait = wait.withTimeout(5000, TimeUnit.MILLISECONDS);
            webDriverFluentWait.pollingEvery(250, TimeUnit.MILLISECONDS);
            wait.ignoring(NoSuchElementException.class);
            wait.ignoring(TimeoutException.class);
            wait.until(ExpectedConditions.visibilityOf(element));

        } catch (NoSuchElementException e) {
            throw new TestToolException(element + " is not visible!");
        }
        return this;
    }

    /**
     * Wait until the element is clickable
     *
     * @param element - web element
     */
    public HelperMethods waitForClickability(WebElement element) {
        try {
            final FluentWait<WebDriver> webDriverFluentWait = wait.withTimeout(5000, TimeUnit.MILLISECONDS);
            webDriverFluentWait.pollingEvery(250, TimeUnit.MILLISECONDS);
            wait.ignoring(NoSuchElementException.class);
            wait.ignoring(TimeoutException.class);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException e) {
            throw new TestToolException(element + " is not clickable!");
        }
        return this;
    }
    /**
     * Send text to the element
     * @param element - web element
     * @param text    - text
     */
    public HelperMethods sendText(WebElement element, String text) {
        try {
            doClick(element);
            element.clear();
            printInfo( text + " has been sent to the element: " + doGetText(element));
            element.sendKeys(text);
        } catch (NoSuchElementException e) {
            throw new TestToolException("Some exception got occurred while getting the web element: " + element + ": " + e.getCause());
        }
        return this;

    }

    /**
     * Send text and press Enter by using Robot Class
     * @param element - web element
     * @param text    - text
     */
    public HelperMethods doSendTextAndPressEnter(WebElement element, String text) {
        try {
            Robot robot = new Robot();
            waitForVisibility(element);
            element.click();
            element.clear();
            element.sendKeys(text);
            waitForClickability(element);
            robot.keyPress(KeyEvent.VK_ENTER);
            waitForClickability(element);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            throw new TestToolException("Error occurred while pressing sending and pressing enter to value: " + text);
        }
        return this;
    }

    /**
     * Refresh the page by using Selenium Send Key Method
     *
     * @param element - web element
     */
    public HelperMethods refreshPageByWebElement(WebElement element) {
        try {
            doClick(element);
            element.sendKeys(Keys.F5);
            waitForVisibility(element);
        } catch (Exception e) {
            throw new TestToolException("Error occurred while pressing refreshing the page with element: " + element + ": " + e.getCause());
        }
        return this;

    }

    /**
     * Click on the web-element by using "Action Class"
     * @param element - web element
     */
    public HelperMethods doClick(WebElement element) {
        try {
            waitForVisibility(element);
            waitForClickability(element);
            actions.click(element).build().perform();
        } catch (NoSuchContextException e) {
            throw new TestToolException("Some exception occurred while clicking to the web element: " + element + ": " + e.getCause());

        }
        return this;
    }
    /**
     * Move and Click the web-element by using "Action Class"
     * @param element - web element
     */

    public HelperMethods moveToElementAndClick(WebElement element) {
        try {
            moveToElement(element);
            doClick(element);

        } catch (NoSuchElementException e) {
            throw new TestToolException("Some exception occurred while moving or clicking to the web element: " + element + ": " + e.getCause());
        }
        return this;
    }
    /**
     * Move to the defined web-element by using "Action Class"
     * @param element - web element
     */
    public HelperMethods moveToElement(WebElement element) {
        try {
            actions.moveToElement(element).pause(Duration.ofMillis(1000)).build().perform();
        } catch (NoSuchElementException e) {
            throw new TestToolException("Some exception occurred while moving  to the web element: " + element + ": " + e.getCause());

        }
        return this;
    }
    /**
     * Navigate to the page and wait till the page is loaded to avoid potential exception error
     */
    public HelperMethods navigateToBackPage() {
        try {
            driver.navigate().back();
            getWaitObject();
        } catch (NoSuchContextException e) {
            throw new TestToolException("Some exception occurred while navigating the the page: " + driver.getCurrentUrl() + ": " + e.getCause());

        }
        return this;
    }

    /**
     * Set browser size to medium
     * Dimension can be modified from configuration.properties file
     */
    public HelperMethods setBrowserSizeAsMedium() {
        try {
            int width = Integer.parseInt(ConfigReader.getProperty("width"));
            int height = Integer.parseInt(ConfigReader.getProperty("height"));
            Dimension dimension = new Dimension(width, height);
            driver.manage().window().setSize(dimension);
        } catch (NoSuchContextException e) {
            throw new TestToolException("Some exception occurred while setting the window size " + ": " + e.getCause());
        }
        return this;
    }

    /**
     * Set browser size to maximize
     * Dimension can be modified from configuration.properties file
     */
    public HelperMethods setBrowserSizeToMaximize() {
        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(Constants.PAGE_LOAD_WAIT_TIME, TimeUnit.SECONDS);
        } catch (NoSuchContextException e) {
            throw new TestToolException("Some exception occurred while setting the window maximize " + ": " + e.getCause());
        }
        return this;
    }

    /**
     * Close cookies if they are appeared during the test execution
     */
    public HelperMethods closeCookies() {
        final String methodName = "MenuPage.closeCookies: ";
        printInfoMethodStarted(methodName);
        try {
            if (driver.findElements(By.xpath("//*[@id='CybotCookiebotDialogBodyButtonAccept']")).size() >= 1) {
                JavaScriptUtil.clickElementByJS(acceptCookies);
                waitForSeconds(3);
                printInfo(methodName + "are closed by clicking on the accept button!");
            }else{
                printInfo(methodName + " Cookie is not visible");
            }
//            if (driver.findElements(By.xpath("//*[contains(@id,'leadinModal')]/div[2]/button")).size() >= 1) {
//                closeTabOnCookies.click();
//                printInfo(methodName + " are disabled!");
//            }
//            else{
//                printInfo(methodName + " Cookie is not visible");
//            }
        } catch (TestToolException e) {
            e.printStackTrace();
            printInfo(methodName + "is failed: " + e.getCause());
        }
        printInfoMethodEnded(methodName);
        return this;
    }

    /**
     * Get Random int Number
     * @param min - min value
     * @param max - max value
     * @return - random number in the range of min and max
     */
    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}




