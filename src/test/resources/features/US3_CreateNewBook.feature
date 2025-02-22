@us3 @smoke @regression @api
Feature: As a librarian, I want to create a new book using the library2.cydeo.com API
  so that I can add new books to the system.

  Scenario Outline: Librarian can create a new book and verify its creation by message and ID
    Given I logged in library2 as "librarian"
    And provided content type is "application/json"
    And provided content type for POST is "application/json"
    And sent body for post a new "book" contains the following data
      | year             | <year>        |
      | author           | <author>      |
      | book_category_id | <category_id> |
      | description      | <description> |
      | name             | <name>        |

    When I use POST request to "add_book"
    Then the response status code should be 200
    And the content type is "application/json"
    And I should see the message "The book has been created."
    And the response should contain a non-null book ID

    Examples:
      | year | author     | category_id | description  | name                  |
      | 1917 | Lenin V.I. | 2           | book         | "The Collected Works" |
      | 2023 | John Smith | 5           | science book | "Advanced Science"    |
      | 1991 | Jane Doe   | 3           | history      | "Historical Records"  |

  @us3 @smoke @regression @api @ui @db
  Scenario: Librarian can create a new book and verify its creation by ID (verifying UI, API, DB at once)
    Given I logged in library2 as "librarian"
    And provided content type is "application/json"
    And provided content type for POST is "application/json"
    And I create a random "book" for API request
    Given I logged in library2 as "librarian" using UI
    And I go to Books page
    When I use POST request to "add_book"
    Then the response status code should be 200
    And the content type is "application/json"
    And I should see the message "The book has been created."
    And being on Books page I provide the name of the added book into search box and search
    And the books data should match between UI, DB and API (searching them by the name)
      | name   |
      | author |
      | year   |