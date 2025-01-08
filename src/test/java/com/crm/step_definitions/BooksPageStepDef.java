package com.crm.step_definitions;

import com.crm.pages.BasePage;
import com.crm.pages.BooksPage;
import com.crm.utilites.BrowserUtil;
import io.cucumber.java.en.Then;
import org.openqa.selenium.Keys;

public class BooksPageStepDef {

    BooksPage bp = new BooksPage();

    @Then("being on Books page I provide the name of the added book into search box and search")
    public void search_for_a_book_using_UI(){

        BrowserUtil.waitUntilVisibly(bp.searchBox, 5);

        String bookName = BasePage.getParamData("name");
        System.out.println("Retrieved shared data: " + bookName);

        bp.searchBox.sendKeys(bookName+ Keys.ENTER);

    }


}
