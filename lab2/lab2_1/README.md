# Exercise 1: Stocks portfolio
In this exercise, we will implement and test the StocksPortfolio class, which holds a collection of stocks and calculates the total value of the portfolio based on the current condition of the stock market.

## Implementation Steps
- Implement the `StocksPortfolio` class and the `IStockMarketService` interface.
- Write a test for the `getTotalValue()` method of `StocksPortfolio` using Mockito to mock the `IStockMarketService` interface.
- Load the mock with the proper expectations using `when...thenReturn`.
- Execute the test and verify the result using JUnit 5 asserts and Mockito's `verify`.

## Key Takeaways
- Unit testing is important to ensure the quality and reliability of software applications.
- Mocking is a technique used to isolate dependencies in unit tests and to ensure that tests have predictable results.
- Mockito is a popular Java mocking framework that simplifies the process of creating and managing mock objects.
- Hamcrest is a library that provides more readable and expressive assertions than the JUnit core assertions.
- When using JUnit 5 with Mockito, the `@ExtendWith(MockitoExtension.class)` annotation should be used to integrate the Mockito framework.
- The `@Mock` annotation is used to create a mock object in Mockito.
- The `when...thenReturn` construct is used to define the behavior of a mock object in a specific test scenario.
- The `verify` method in Mockito is used to ensure that a specific method of a mock object has been called with the expected arguments.