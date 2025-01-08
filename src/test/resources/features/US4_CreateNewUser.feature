@us4 @smoke @regression
Feature: Feature: As a librarian, I want to create a new user using the library2.cydeo.com API
  so that I can add new users to the system.

  @us4 @smoke @regression @scenario1 @api
  Scenario: as a librarian, I can create a new user using API
    Given I logged in library2 as "librarian"
    And provided content type for POST is "application/json"
    And provided content type is "application/json"
    And sent body for post a new "user" contains the following data
      | full_name     | Oleg Gozmanov                 |
      | email         | it'll be random               |
      | password      | ribaMech                      |
      | user_group_id | 2                             |
      | status        | Krutoi                        |
      | start_date    | 2020-01-04                    |
      | end_date      | 2030-01-04                    |
      | address       | Pushkin st. Kolotushkin house |
    When I use POST request to "/add_user"
    Then the response status code should be 200
    And the content type is "application/json"
    And I should see the message "The user has been created."

  @us4 @smoke @regression @scenario2 @db @api @ui
  Scenario Outline: as a librarian, I can create a new user using API
    Given I logged in library2 as "librarian"
    And provided content type for POST is "application/json"
    And provided content type is "application/json"
    And sent body for post a new "user" contains the following data
      | full_name     | <name>      |
      | email         | <email>     |
      | password      | <password>  |
      | user_group_id | <userGroup> |
      | status        | <status>    |
      | start_date    | <startDate> |
      | end_date      | <endDate>   |
      | address       | <address>   |
    When I use POST request to "/add_user"
    Then the response status code should be 200
    And the content type is "application/json"
    And I should see the message "The user has been created."
    And created "user" info should match with Database
    And I should be able to login as the newly created user
    And I click on Users link page
    And I should see the user's name should appear in the Dashboard Page
    Examples:
      | name       | email           | password  | userGroup | status  | startDate  | endDate    | address                |
      | Cm. Akulov | it'll be random | pass123   | 2         | active  | 2023-01-01 | 2025-01-01 | 123 Main St, Las Vegas |
      | Cm. Nosov  | it'll be random | abc$456   | 2         | pending | 2024-06-01 | 2026-06-01 | 456 Elm St, New York   |
      | Cm. Vlasov | it'll be random | secure789 | 2         | active  | 2023-09-15 | 2027-09-15 | 789 Pine St, Chicago   |
