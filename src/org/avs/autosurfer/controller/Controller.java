package org.avs.autosurfer.controller;

import org.avs.autosurfer.view.ExitListener;
import org.avs.autosurfer.view.LoginFormEvent;
import org.avs.autosurfer.view.LoginListener;
import org.avs.autosurfer.view.StartListener;
import org.avs.autosurfer.view.StopListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * Creates a Controller-part of the application and declares its behavior utilizing Selenium WebDriver 2.53.1.
 * 
 * @author Andrey Skvortsov
 * 
 */
public class Controller implements LoginListener, StartListener, StopListener, ExitListener
{
    private final long DELAY = 10000;
    
    private WebDriver driver;
    
    private String parentWindowHandle;

    @Override
    public void loginPerformed(LoginFormEvent event)
    {
        try
        {
            if (driver == null)
            {
                System.setProperty("webdriver.chrome.driver", event.getDriverPath());
                driver = new ChromeDriver();
            }

            driver.get("https://www.neobux.com/m/l/?vl"); // Opens Neobux.com login page.
            parentWindowHandle = driver.getWindowHandle();

            WebDriverWait wait = new WebDriverWait(driver, 10); // Waits up to 10 seconds before throwing
                                                                // TimeoutException or if it finds the elements will
                                                                // return it in 0 - 10 seconds.

            // Locates and fills in Username (id = Kf1) and Password (id = Kf2) fields.
            wait.until(ExpectedConditions.elementToBeClickable(By.id("Kf1"))).sendKeys(event.getUsername());
            wait.until(ExpectedConditions.elementToBeClickable(By.id("Kf2")))
                    .sendKeys(String.valueOf(event.getPassword()));

            // Locates and clicks Send/Login (id = botao_login) button in the case CAPTCHA (id = Kf3) field is not
            // displayed. Otherwise, delays for 10 seconds and clicks Send/Login button.
            if (!isElementPresentsById("Kf3"))
            {
                wait.until(ExpectedConditions.elementToBeClickable(By.id("botao_login"))).click();
            } else
            {
                // Note: instead of sleep() invocation a method dealing with CAPTCHA automatically should be called.
                Thread.sleep(DELAY);
                driver.findElement(By.id("botao_login")).click();
            }
        } catch (Exception e)
        {
            if (driver != null)
            {
                exitPerformed();                
            }
        }
    }

    @Override
    public void startPerformed(boolean isWatchAllAdsChecked, boolean isWatchAllAdsPrizesChecked)
    {
        if (((ChromeDriver)driver).getSessionId() != null)
        {
            try
            {
                // Waits until the main page is loaded using id (id = t_conta) for a name of a user field.
                WebDriverWait wait = new WebDriverWait(driver, 20);
                wait.until(ExpectedConditions.elementToBeClickable(By.id("t_conta")));

                // Opens View Advertisements tab using xPath query because no id for a span element was found.
                if (driver.findElement(By.xpath("//span[text()='View Advertisements']")).isDisplayed()
                        && driver.findElement(By.xpath("//span[text()='View Advertisements']")).isEnabled())
                {
                    wait.until(
                            ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='View Advertisements']")))
                            .click();

                    // Note: When both checked and Stop is clicked in the process of wathcAllAds(), watchAllAdsPrizes()
                    // starts anyway. Must be handled in a proper way.
                    if (isWatchAllAdsChecked)
                    {
                        watchAllAds();
                    }
                    if (isWatchAllAdsPrizesChecked)
                    {
                        watchAllAdsPrizes();
                    }
                }
            } catch (Exception e)
            {
                if (driver != null)
                {
                    stopPerformed();
                }
            }
        }
    }

    @Override
    public void stopPerformed()
    {
        if (driver != null)
        {
            // Closes all opened tabs but a parent one and switches handle to it.
            for (String windowHandle : driver.getWindowHandles())
            {
                if (!windowHandle.equals(parentWindowHandle))
                {
                    driver.switchTo().window(windowHandle);
                    driver.close();
                }
                driver.switchTo().window(parentWindowHandle);
            }
        }
    }

    @Override
    public void exitPerformed()
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, 10);

            // Clicks "Power" button using className and confirms logout.
            if (isElementPresentsByClassName("ic-power"))
            {
                wait.until(ExpectedConditions.elementToBeClickable(By.className("ic-power"))).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.className("confirm"))).click();

                // Checks that logout is completed by ensuring that the main page is loaded.
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Login']"))).click();
                driver.quit();
                driver = null;
            }
        } catch (WebDriverException e)
        {
            if (driver != null)
            {
                driver.quit();
                driver = null;
            }
        }
    }

    private void watchAllAds()
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, 20);

            // Locates and clicks an advertisement (id = daXa, where X - number of the advertisement) box.
            int advertisementID = 1;
            while (isElementPresentsById("da" + advertisementID + "a"))
            {
                wait.until(ExpectedConditions.elementToBeClickable(By.id("da" + advertisementID + "a"))).click();
                Thread.sleep(3000);

                // Locates and clicks the red circle (id = lY, where Y - number of the advertisement) for verification
                // and display of the advertisement.
                wait.until(ExpectedConditions.elementToBeClickable(By.id("l" + advertisementID))).click();

                // Waits until the advertisement is verified (20 seconds) and closes the tab using AWT Robot class.
                Thread.sleep(DELAY * 2);

                for (String windowHandle : driver.getWindowHandles())
                {
                    if (!parentWindowHandle.equals(windowHandle))
                    {
                        driver.switchTo().window(windowHandle);
                        driver.close();
                    }
                    driver.switchTo().window(parentWindowHandle);
                }
                Thread.sleep(DELAY / 4);
                advertisementID++;
            }
        } catch (Exception e)
        {
            stopPerformed();
        }
    }

    private void watchAllAdsPrizes()
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, 10);

            // Locates and clicks the AdsPrize counter (id = ap_hct).
            while (isElementPresentsById("ap_hct"))
            {
                wait.until(ExpectedConditions.elementToBeClickable(By.id("ap_hct"))).click();

                // Waits for 3 seconds until AdsPrize page is loaded.
                Thread.sleep(3000);

                // Switches WindowHadle to the loaded AdsPrize tab.
                for (String windowHandle : driver.getWindowHandles())
                {
                    driver.switchTo().window(windowHandle);
                }
                watchAdsPrize();
            }
        } catch (Exception e)
        {
            stopPerformed();
        }
    }

    private void watchAdsPrize()
    {
        try
        {
            // Waits until AdsPrize page is loaded and the first AdsPrize is verified.
            Thread.sleep(DELAY);

            // Locates and clicks Next button using linkText query because no id for the button element was found.
            // Clicks until there is such button (other cases would be a. AdsPrize is won, b. there are no more
            // AdzPrizes).
            while (driver.findElement(By.linkText("Next")).isDisplayed()
                    && driver.findElement(By.linkText("Next")).isEnabled())
            {
                driver.findElement(By.linkText("Next")).click();

                // Waits until the AdsPrize is verified (20 seconds)
                Thread.sleep(DELAY * 2);
            }
        } catch (Exception e)
        {
            stopPerformed();
        }
    }

    // Locates element on a Web page using its id.
    private boolean isElementPresentsById(String id)
    {
        if (driver.findElement(By.id(id)).isDisplayed() && driver.findElement(By.id(id)).isEnabled())
        {
            return true;
        } else
        {
            return false;
        }
    }

    // Locates element on a Web page using its className.
    private boolean isElementPresentsByClassName(String className)
    {
        if (driver.findElement(By.className(className)).isDisplayed()
                && driver.findElement(By.className(className)).isEnabled())
        {
            return true;
        } else
        {
            return false;
        }
    }
}