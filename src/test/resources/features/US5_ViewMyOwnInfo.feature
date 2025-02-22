@us5 @api @smoke @regression
  Feature: As a user, I want to view my own user information using the API
    so that I can see what information is stored about me.
    
  @us5 @smoke @regression @api
  Scenario: as a user I'm able to view my information using API
    Given I logged in library2 as "librarian"
    And provided content type for POST is "application/json"
    And provided content type is "application/json"
    And I create a random "librarian" for API request
    When I use POST request to "/add_user"
    Then I should see the message "The user has been created."
    And the response status code should be 200
    And the content type is "application/json"
    Then I login as newly created user
    And I send a token to the body request
    When I use POST request to "/decode"
    Then the content type is "application/json"
    And the response status code should be 200
    And I should see my "email" in the response body

    