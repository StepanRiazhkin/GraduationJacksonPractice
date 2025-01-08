package com.crm.pages;

import com.crm.utilites.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class BooksPage extends BasePage {

    @FindBy(xpath = "//input[@type='search']")
    public WebElement searchBox;

    public static Map<String, Object> getBookNameAuthYearInfo(String bookName) {
        Map<String, Object> bookInfo = new LinkedHashMap<>();

        // Initialize WebDriverWait once
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));

        for (int i = 3; i < 6; i++) {
            if (i == 5) {
                i++; // Skip column 5 as per your logic
            }

            // Wait for header and cell to be visible
            By headerLocator = By.xpath("//table/thead/tr/th[" + i + "]");
            By cellLocator = By.xpath("//table/tbody/tr[1]/td[" + i + "]");
            By expectedName = By.xpath("//table/tbody/tr[1]/td[3]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(headerLocator));
            wait.until(ExpectedConditions.visibilityOfElementLocated(cellLocator));

            // Wait until the cell contains the desired text
            wait.until(ExpectedConditions.textToBePresentInElementLocated(expectedName, bookName));

            // Retrieve the header and cell values
            String key = Driver.getDriver().findElement(headerLocator).getText().toLowerCase();
            String value = Driver.getDriver().findElement(cellLocator).getText();

            bookInfo.put(key, value);
        }

        return bookInfo;
    }
}
