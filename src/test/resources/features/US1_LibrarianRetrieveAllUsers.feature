@us1 @smoke @regression @api
Feature: As a librarian, I want to retrieve all users from the library2.cydeo.com API endpoint
  so that I can display them in my application.

  Scenario: as a librarian I can retrieve all users from library2.cydeo.com
    Given I logged in library2 as "librarian"
    And provided content type is "application/json"
    When I use GET request to "/get_all_users"
    Then the response status code should be 200
    And the content type is "application/json"
    And data shouldn't be null
      | id   |
      | name |
