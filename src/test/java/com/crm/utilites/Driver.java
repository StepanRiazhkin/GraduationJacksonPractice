package com.crm.utilites;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public class Driver {

    private Driver() {
    }

    public static final InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver() {

        String gridAddress;

        if (driverPool.get() == null) {

            String browser;
            if (System.getProperty("browser") != null) {
                browser = System.getProperty("browser");
            } else {
                browser = ConfigReader.getProperty("browser");
            }

            switch (browser) {

                case "remote-chrome":
                    try {

                        gridAddress = "100.29.38.166";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--start-maximized");
                        driverPool.set(new RemoteWebDriver(url, chromeOptions));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "remote-firefox":

                    try {

                        gridAddress = "54.162.50.13";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.addArguments("--start-maximized");
                        driverPool.set(new RemoteWebDriver(url, firefoxOptions));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "chrome":
                    driverPool.set(new ChromeDriver());
                    break;

                case "headless-chrome":
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless=new");
                    driverPool.set(new ChromeDriver(options));
                    break;

                case "firefox":
                    driverPool.set(new FirefoxDriver());
                    break;

                default:
                    System.err.println("No such driver type provided in Driver class");
                    throw new IllegalArgumentException("available types: chrome, firefox, remote-browser");
            }

            driverPool.get().manage().window().maximize();
            driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        }

        return driverPool.get();

    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove(); // Clear the reference after quitting
        }
    }

}
