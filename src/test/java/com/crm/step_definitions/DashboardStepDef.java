package com.crm.step_definitions;

import com.crm.pages.DashBoardPage;
import com.crm.pages.UserManagement;
import com.crm.utilites.BrowserUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class DashboardStepDef {

    DashBoardPage dbPage = new DashBoardPage();
    UserManagement userManagement = new UserManagement();

    @Given("I go to Books page")
    public void i_go_to_books_page() {

        BrowserUtil.waitUntilVisibly(dbPage.booksPageLink, 5);
        dbPage.booksPageLink.click();

    }

    @Then("I click on Users link page")
    public void i_click_on_users_link_page() {

        userManagement.usersLinkPage.click();

    }

}
