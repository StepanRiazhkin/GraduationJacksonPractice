package com.crm.pages;

import com.crm.utilites.ConfigReader;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.InputMismatchException;

public class LibraryLoginPage extends BasePage{

    @FindBy(id = "inputEmail")
    public  WebElement emailBox;

    @FindBy(id = "inputPassword")
    public WebElement passBox;

    @FindBy(xpath = "//button[text()='Sign in']")
    public WebElement loginButton;

    public void login(String userType){

        if (userType.equals("librarian")) {

            emailBox.sendKeys(ConfigReader.getProperty("librarianLogin"));
            passBox.sendKeys(ConfigReader.getProperty("librarianPassword"));
            loginButton.click();

        } else {
            throw new InputMismatchException("Invalid Credentials");
        }
    }

    public void loginNewUser(){

        System.out.println(
                "Provided credentials: \nemail - "+BasePage.getParamData("email")+"\npassword - "+BasePage.getParamData("password")
        );

        emailBox.sendKeys(BasePage.getParamData("email"));
        passBox.sendKeys(BasePage.getParamData("password"));
        loginButton.click();

    }

}
