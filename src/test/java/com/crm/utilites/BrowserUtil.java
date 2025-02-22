package com.crm.utilites;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BrowserUtil {

    public static void sleep(int seconds) {

        try {

            Thread.sleep(seconds * 100L);

        } catch (InterruptedException ignored) {

        }

    }

    public static void waitUntilVisibly(WebElement element, int seconds) {

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(seconds));

        wait.until(ExpectedConditions.visibilityOf(element));

    }

    public static WebElement waitUntilVisibly(By by, int waitSeconds, int maxRetry) {

        int currentRetry = 0;

        while (currentRetry <= maxRetry) {

            try {

                WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(waitSeconds));
                return wait.until(ExpectedConditions.presenceOfElementLocated(by));

            } catch (TimeoutException e) {

                System.out.println("Locating an element...\nAttempt: " + currentRetry + 1);
                currentRetry++;

            }

        }

        throw new NoSuchElementException("Element isn't found.\nWaite time: " + waitSeconds + "\nRetries: " + currentRetry);

    }

    public static boolean isValidUploadFileType(@org.jetbrains.annotations.NotNull String fileName, @NotNull List<String> allowedFileTypes){
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        return allowedFileTypes.contains(fileType);
    }

    public static boolean isValidUploadFileSize(long fileSize, long maxFileSize){
        return fileSize<=maxFileSize;
    }

    public static boolean isFileUploaded(@NotNull WebElement uploadButton, String filePath, @NotNull WebElement successMessage){
        uploadButton.sendKeys(filePath);
        return successMessage.isDisplayed();
    }

}
