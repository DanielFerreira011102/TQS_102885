Feature: Remove books
  To keep the library inventory up to date, they need to be able to remove books that are no longer in circulation.

  Scenario: Remove a book
    Given the following books:
      | title | author | published |
      | One good book | Anonymous | 14 March 2013 |
      | Some other book | Tim Tomson | 23 August 2014 |
    When the customer searches for books published between 2013 and 2014
    Then 2 books should have been found
    When the customer removes the book 'Some other book'
    And the customer searches for books published between 2013 and 2014
    Then 1 book should have been found
    And Book 1 should have the title 'One good book'