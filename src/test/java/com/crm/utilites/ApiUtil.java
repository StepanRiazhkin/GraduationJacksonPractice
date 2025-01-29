package com.crm.utilites;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.*;
import static com.crm.utilites.ConfigReader.*;
import static org.hamcrest.Matchers.*;

public class ApiUtil {

    private static final Faker faker = new Faker();

    public static Map<String, String> getCredentials(String role) {

        Map<String, String> credentials = new HashMap<>();

        switch (role) {

            case "librarian":
                credentials.put("email", getProperty("librarianLogin"));
                credentials.put("password", getProperty("librarianPassword"));
                break;
            case "admin":
                credentials.put("adminLogin", System.getenv("ADMIN_LOGIN"));
                credentials.put("adminPassword", System.getenv("ADMIN_PASSWORD"));
                break;
            default:
                System.err.println("No such credential according to the provided role: " + role);
                throw new InputMismatchException("available: admin, librarian");
        }

        return credentials;

    }

    public static String token(String email, String password) {

        JsonPath jsonPath = given()
                .log().ifValidationFails()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract().jsonPath();


        return jsonPath.getString("token");

    }

    public static String getToken(String userType) {

        Map<String, String> credentials = getCredentials(userType);
        String email = credentials.get("email");
        String password = credentials.get("password");

        return token(email, password);

    }

    public static void testBodyNotNull(ValidatableResponse thenPart, List<String> data) {

        for (String each : data) {

            Object value = thenPart.extract().path(each);

            System.out.println("value of the body = " + value);

            if (value instanceof List) {


                thenPart.assertThat()
                        .body(each, everyItem(notNullValue()));


            } else {


                thenPart.assertThat()
                        .body(each, notNullValue());

            }
        }
    }

    public static void validateMessage(ValidatableResponse thenPart, String messagePath, String expectedMessage) {

        thenPart.assertThat()
                .body(
                        messagePath, is(expectedMessage)
                );

    }

    public static JsonPath getItemById(RequestSpecification givenPart, String id) {
        Response response = givenPart
                .accept(ContentType.JSON)
                .pathParam("itemId", id)
                .get("/get_book_by_id/{itemId}");

        System.out.println("Response: " + response.asString());

        response.then()
                .log().ifValidationFails()
                .body("id", equalTo(id));

        return response.jsonPath(); // Return JsonPath
    }

    public static Map<String, Object> bookInfoRandom() {

        Map<String, Object> book = new LinkedHashMap<>();

        int randomYear = ThreadLocalRandom.current().nextInt(1, 2025 + 1);

        book.put("year", String.valueOf(randomYear));
        book.put("author", faker.name().fullName());
        book.put("book_category_id", faker.number().numberBetween(1, 20));
        book.put("description", "Book");
        book.put("name", faker.name().firstName() + " " + faker.book().title() + " " + faker.name().lastName());

        System.out.println("Generated Book Data: " + book);

        return book;
    }


    public static Map<String, Object> getMapNameAuthYear(RequestSpecification givenPart, JsonPath js, String id) {

        System.out.println("Provided Book ID: " + id);


        Map<String, Object> map = new LinkedHashMap<>();

        js = getItemById(givenPart, id);

        map.put("name", js.getString("name"));
        map.put("author", js.getString("author"));
        map.put("year", js.getString("year"));

        return map;

    }

    public static Map<String, Object> getRandomUserInfo(String userType) {

        if (!(userType.equalsIgnoreCase("user") || userType.equalsIgnoreCase("librarian"))) {

            System.err.println("Invalid user group id");
            throw new InputMismatchException("allowed only: 1 or 2");

        } else {

            Map<String, Object> userInfo = new LinkedHashMap<>();
            int userGroupId;

            if (userType.equalsIgnoreCase("user")) {
                userGroupId = 1;
            } else {
                userGroupId = 2;
            }

            System.out.println("Chosen id group: " + userGroupId + " (" + userType + ")");

            String startDate = String.format("%d-%02d-%02d",
                    faker.number().numberBetween(2019, 2024),
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28));

            String endDate = String.format("%d-%02d-%02d",
                    faker.number().numberBetween(2026, 2030),
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28));

            userInfo.put("full_name", faker.name().fullName() + faker.number().numberBetween(1, 2000));
            userInfo.put("email", "YaLubluKirkorova" + faker.number().numberBetween(1, 2000) + "@gmail.com");
            userInfo.put("password", faker.number().numberBetween(100000, 9999999) + faker.name().username());
            userInfo.put("user_group_id", userGroupId);
            userInfo.put("status", "ACTIVE");
            userInfo.put("start_date", startDate);
            userInfo.put("end_date", endDate);
            userInfo.put("address", faker.address().fullAddress());

            System.out.println("Randomly Generated User Info and Key Type: ");
            mapKeyType(userInfo);

            return userInfo;

        }

    }

    public static Map<String, Object> refactorInfoIdObject(Map<String, String> info) {

        Map<String, Object> newInfo = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.equals("user_group_id")) {
                newInfo.put(key, Integer.parseInt(value));
            } else if (key.equals("email")) {
                newInfo.put(key, "YaLubluKirkorova" + faker.number().numberBetween(1, 999999) + "@gmail.com");
            } else {
                newInfo.put(key, value);
            }
        }

        return newInfo;
    }

    public static void mapKeyType(Map<String, Object> map){

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue() + ", Type: " + entry.getValue().getClass().getSimpleName());
        }

    }

}
