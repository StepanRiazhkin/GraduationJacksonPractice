package com.crm.step_definitions;

import com.crm.pages.BasePage;
import com.crm.pages.BooksPage;
import com.crm.utilites.ApiUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.crm.utilites.ApiUtil.*;
import static com.crm.utilites.DB_Util.getMap;
import static com.crm.utilites.DB_Util.getRowMap;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ApiStepDefinitions {

    private RequestSpecification givenPart = given().log().ifValidationFails();
    private ValidatableResponse thenPart;
    private Response response;
    private JsonPath js;
    private String id;
    private String itemType;
    private String token;
    private Map<String, Object> parameters = new LinkedHashMap<>();

    /**
     * ----------------------------------BASE PARTS--------------------------------------
     **/

    @Given("I logged in library2 as {string}")
    public void i_logged_in_as(String credentials) {
        givenPart.header("x-library-token", ApiUtil.getToken(credentials));
    }

    @Given("provided content type is {string}")
    public void provided_content_type_is(String contentType) {
        givenPart.accept(contentType);
    }

    @Then("the response status code should be {int}")
    public void status_code_is(int expectedStatus) {
        thenPart.log().ifValidationFails().statusCode(expectedStatus);
    }

    @Then("the content type is {string}")
    public void content_type_is(String expectedContentType) {
        thenPart.contentType(expectedContentType);
    }

    /**
     * ----------------------------US1: Retrieve All Users--------------------------------
     **/

    @When("I use GET request to {string}")
    public void i_use_get_request_to(String endpoint) {
        response = givenPart.get(endpoint);
        js = response.jsonPath();
        thenPart = response.then().log().ifValidationFails();
    }

    @Then("data shouldn't be null")
    public void shouldn_t_be_null(List<String> bodyData) {
        ApiUtil.testBodyNotNull(thenPart, bodyData);
    }

    /**
     * ----------------------------US2: Find Users by Email/Name--------------------------
     **/

    @Given("provided request parameter as {string} is {string}")
    public void provided_request_parameter_is(String paramName, String paramValue) {
        givenPart.pathParam(paramName, paramValue);
        parameters.put(paramName, paramValue);
    }

    @Then("the {string} field in the response should be the same as the path param {string}")
    public void the_field_is_the_same_as_in_the_response(String fieldName, String paramName) {
        String expectedValue = (String) parameters.get(paramName);
        String actualValue = response.jsonPath().getString(fieldName);
        assertEquals(expectedValue, actualValue);
    }

    /**
     * ----------------------------US3: Create a New Book---------------------------------
     **/

    @Given("provided content type for POST is {string}")
    public void provided_content_type_for_post_is(String contentType) {
        givenPart.contentType(contentType);
    }

    @Given("sent body for post a new {string} contains the following data")
    public void sent_body_for_post_a_new_item_contains_the_following_data(String itemType, Map<String, String> newInfo) {
        parameters = refactorInfoIdObject(newInfo);
        mapKeyType(parameters);
        BasePage.setParamData(parameters);
        givenPart.body(parameters);
        this.itemType = itemType;
    }

    @When("I use POST request to {string}")
    public void i_use_post_request_to(String endpoint) {
        response = givenPart.post(endpoint);
        thenPart = response.then();
        js = response.jsonPath();

        if (itemType.equalsIgnoreCase("book")) {
            id = js.getString("book_id");
        } else if (itemType.equalsIgnoreCase("user") || itemType.equalsIgnoreCase("librarian")) {
            id = js.getString("user_id");
        } else {
            throw new IllegalStateException("No such item type. Allowed only: user, book");
        }

    }

    @Then("I should see the message {string}")
    public void i_should_see_the_message(String expectedMessage) {
        ApiUtil.validateMessage(thenPart, "message", expectedMessage);
    }

    @Then("the response should contain a non-null book ID")
    public void the_response_should_contain_a_non_null_book_id() {
        id = js.getString("book_id");
        ApiUtil.getItemById(givenPart, id);
    }

    /**
     * -----------------US3: Verify Book Data (UI, DB, API); US4: Create A New User------------------
     **/

    @Given("I create a random {string} for API request")
    public void i_create_a_random_item_for_api_request(String itemType) {
        if (itemType.equalsIgnoreCase("book")) {
            parameters = ApiUtil.bookInfoRandom();
            BasePage.setParamData("name", (String) parameters.get("name"));
        } else {
            parameters = ApiUtil.getRandomUserInfo(itemType);
            BasePage.setParamData("full_name", (String) parameters.get("full_name"));
        }
        this.itemType = itemType;
        givenPart.body(parameters);
    }

    @Then("the books data should match between UI, DB and API \\(searching them by the name)")
    public void the_books_data_should_match_between_ui_db_and_api(List<String> matchDataType) {
        String bookName = BasePage.getParamData("name");
        Map<String, Object> bookUi = getBookDataFromUI(bookName);
        Map<String, Object> bookDb = getBookDataFromDB(bookName, matchDataType);
        Map<String, Object> bookApi = getBookDataFromAPI(id);

        assertEquals(bookUi, bookDb);
        assertEquals(bookDb, bookApi);
    }

    /**
     * --------------------US4: Create A New User (DB, UI, API Verify)---------------------
     **/

    @Then("created {string} info should match with Database")
    public void created_item_info_should_match_with_database(String itemType) {
        if (itemType.equalsIgnoreCase("user")) {
            assertEquals(getUserDataFromAPI(id), getUserDataFromDb(id));
        } else if (itemType.equalsIgnoreCase("book")) {
            System.err.println("Book Implementation is not ready yet.");
        } else {
            throw new InputMismatchException("Invalid object type");
        }
    }

    /**
     * --------------------------US5: Being A User View My Info--------------------------
     **/

    @Then("I login as newly created user")
    public void i_login_as_newly_created_user() {
        String email = (String) parameters.get("email");
        String password = (String) parameters.get("password");
        token = token(email, password);

        givenPart = given().log().ifValidationFails();
        givenPart.header("x-library-token", ApiUtil.token(email, password));
    }

    @Then("I send a token to the body request")
    public void i_send_a_token_to_the_body_request() {
        givenPart.contentType(ContentType.URLENC).formParam("token", token);
    }

    @Then("I should see my {string} in the response body")
    public void i_should_see_my_in_the_response_body(String info) {
        String actualEmail = js.getString(info);
        String expectedEmail = (String) parameters.get("email");
        assertEquals(actualEmail, expectedEmail);
    }

    /**
     * -------------------------HELPER METHODS FOR ISOLATION-----------------------------
     **/

    private Map<String, Object> getUserDataFromDb(String id) {
        String query = "SELECT * FROM users WHERE id = '" + id + "';";
        return getRowMap(query, 1);
    }

    private Map<String, Object> getUserDataFromAPI(String id) {
        Response response = given()
                .header("x-library-token", getToken("librarian"))
                .accept(ContentType.JSON)
                .pathParam("id", id)
                .get("/get_user_by_id/{id}");

        if (response.body().asString().isEmpty()) {
            throw new IllegalStateException("API returned an empty response for ID: " + id);
        }

        return response.jsonPath().getMap("");
    }

    private Map<String, Object> getBookDataFromUI(String bookName) {
        return BooksPage.getBookNameAuthYearInfo(bookName);
    }

    private Map<String, Object> getBookDataFromDB(String bookName, List<String> matchDataType) {
        String query = String.format(
                "SELECT %s, %s, %s FROM books WHERE NAME = '%s';",
                matchDataType.get(0), matchDataType.get(1), matchDataType.get(2), bookName
        );
        return getMap(query, 1);
    }

    private Map<String, Object> getBookDataFromAPI(String bookId) {
        return ApiUtil.getMapNameAuthYear(givenPart, js, bookId);
    }
}
