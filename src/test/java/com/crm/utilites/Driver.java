package com.crm.utilites;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.URL;
import java.time.Duration;

public class Driver {

    private Driver(){};

    public static final InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver(){

        if(driverPool.get() == null){

            String browser;
            if (System.getProperty("browser") != null) {
                browser = System.getProperty("browser");
            } else {
                browser = ConfigReader.getProperty("browser");
            }

            switch (browser){

                case "remote-browser":
                    try{

                        URL url = new URL("http://174.129.57.20:4444/wd/hub");


                    }catch (Exception e){

                        e.printStackTrace();

                    }
                    break;
                case "chrome":
                    driverPool.set(new ChromeDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
                case "firefox":
                    driverPool.set(new FirefoxDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                default:
                    System.err.println("No such driver type provided in Driver class");
                    throw new IllegalArgumentException("available types: chrome, firefox, remote-browser");
            }

            return driverPool.get();

        }else{

            return driverPool.get();

        }

    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove(); // Clear the reference after quitting
        }
    }

}
