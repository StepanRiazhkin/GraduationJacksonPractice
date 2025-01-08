package com.crm.step_definitions;

import com.crm.pages.LibraryLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class LibraryLoginPageStepDef {

    LibraryLoginPage loginPage = new LibraryLoginPage();

    @Given("I logged in library2 as {string} using UI")
    public void i_logged_in_library2_as_using_ui(String string) {

        loginPage.login(string);

    }

    @Then("I should be able to login as the newly created user")
    public void i_should_be_able_to_login_as_the_newly_created_user() {

        loginPage.loginNewUser();

    }


}
