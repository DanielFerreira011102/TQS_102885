Feature: Air Quality App Test

  Scenario: Verify app functionality
    Given I am on the Air Quality Test App
    When I search for "Viseu"
    And I wait for 5 seconds
    And I click on the Defra, EPA, and Cache links
    Then the request count should be "1"
    And the cache hits count should be "0"
    And the cache misses count should be "1"
    And the expired count should be "0"
    And I click on the Search link
    When I search for "Lisbon"
    And I click on the Search link
    When I search for "Viseu"
    Then the city text should be "VISEU, PT"
    And EPA should be present
    And I click on the Cache link
    Then the request count should be "3"
    And the cache hits count should be "1"
    And the cache misses count should be "2"
    And the expired count should be "0"
    And I click on the Search link
    When I search for "fffffffffffffffff"
    Then the error message should be "ERROR 400: Bad Request"
    And I click on the Cache link
    Then the request count should be "4"
    And the cache hits count should be "1"
    And the cache misses count should be "3"
    And the expired count should be "0"
