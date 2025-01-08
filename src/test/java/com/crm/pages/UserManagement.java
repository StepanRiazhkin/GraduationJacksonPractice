package com.crm.pages;

import com.crm.utilites.Driver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserManagement extends BasePage {

    public String newUserEmail = BasePage.getParamData("email");

    @FindBy(xpath = "//a[@href='#users']")
    public WebElement usersLinkPage;

    public void isSpecificUser(String email) {

        int rowIndex = 1;

        while (true) {
            try {

                WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]/td[4]")));

                WebElement cell = Driver.getDriver().findElement(By.xpath("//table/tbody/tr[" + rowIndex + "]/td[4]"));

                if (cell.getText().equals(email)) {  // Check if the cell's text matches the given name

                    Assert.assertTrue(cell.isEnabled()); // Verify the element is enabled

                    return;

                }

                rowIndex++; // Move to the next row

            } catch (NoSuchElementException e) {

                break;

            }
        }

        throw new NoSuchElementException("No such element with the name: " + newUserEmail);

    }


}
