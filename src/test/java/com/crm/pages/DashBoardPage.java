package com.crm.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashBoardPage extends BasePage{

    @FindBy(xpath = "//a[@href='#books']")
    public WebElement booksPageLink;

}
