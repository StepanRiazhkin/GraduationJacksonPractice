package com.crm.utilites;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BrowserUtil {

    public static void sleep(int seconds){

        try{

            Thread.sleep(seconds* 100L);

        }catch (InterruptedException ignored){

        }

    }

    public static void waitUntilVisibly(WebElement element, int seconds){

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(seconds));

        wait.until(ExpectedConditions.visibilityOf(element));

    }

    public static void searchForNew(String name){



    }

}
