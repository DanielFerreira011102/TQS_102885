Feature: Blazerunner Feature

  This feature tests the flight booking process on Blazedemo website.
  It ensures that the user can select the desired flight, fill in the passenger details, and complete the booking successfully.

  # I could have put more parameters/arguments. But I found it unproductive since the purpose of Cucumber is to make
  # the test more readable and easy to grasp. For example, adding parameters for every field in the
  # "I fill in the passenger details" statement, would make the feature more complex. And, the result should not even
  # depend on that input. I assume that the personal information of the customer is not considered in the final price
  # of the flight, so they should not represent a different scenario.

  Scenario Outline: Purchase a flight
    Given I am on the Blazedemo homepage
    When I select the departure city as "<departure_city>" and arrival city as "<arrival_city>"
    And I click on the Find Flights button
    And I select the flight <flight_number> from the list
    And I fill in the passenger details
    And I click on the Remember Me button
    And I confirm my booking
    Then I should see the confirmation page with the price 555 USD
    And I should be on the confirmation page
    And I close the browser

    # Why does it not detect "<flight_price>" as a placeholder and throws an UndefinedStepException???
    Examples:
      | departure_city | arrival_city | flight_number | flight_price
      | Boston         | Berlin       | 1             | 555