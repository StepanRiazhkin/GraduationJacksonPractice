@us2 @smoke @regression @api
Feature: Feature: As a user, I want to search for a specific user by their name or email address
  using the API so that I can quickly find the information I need.

  Scenario: as a user I can find a specific user by their name or email
    Given I logged in library2 as "librarian"
    And provided content type is "application/json"
    And provided request parameter as "id" is "1"
    When I use GET request to "/get_user_by_id/{id}"
    Then the content type is "application/json"
    And the response status code should be 200
    And the "id" field in the response should be the same as the path param "id"
    And data shouldn't be null
      | full_name  |
      | email |