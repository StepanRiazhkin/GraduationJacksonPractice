package com.crm.step_definitions;

import com.crm.pages.UserManagement;
import io.cucumber.java.en.Then;

public class UsersPageStepDef {

    UserManagement userManage = new UserManagement();

    @Then("I should see the user's name should appear in the Dashboard Page")
    public void i_should_see_the_user_s_name_should_appear_in_the_dashboard_page() {

        userManage.isSpecificUser(userManage.newUserEmail);

    }


}
