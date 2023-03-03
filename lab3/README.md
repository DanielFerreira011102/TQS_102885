# Lab 3: Multi-layer application testing (with Spring Boot)

This lab explores testing techniques in Spring Boot applications, including loading application contexts, using annotations such as @SpringBootTest, @DataJpaTest, and @WebMvcTest, and isolating functionality to be tested.

## Learning Objectives
- Understand how to use Spring Boot testing annotations to improve performance and isolate functionality.
- Learn how to use the AssertJ library to create expressive assertions in tests.
- Practice a TDD approach to develop Spring Boot applications.

## Key Points
- Limiting application contexts to a set of Spring components improves performance.
- Use @DataJpaTest to test only @Repository spring components.
- Use @WebMvcTest to test Rest APIs exposed through Controllers, with mocked beans.
- AssertJ library to create expressive assertions in tests.
- Use a TDD approach: write the test first; make sure the project can compile without errors; defer the actual implementation of production code as much as possible.

## Notes
- The TDD approach involves writing the test first, ensuring the project can compile without errors, and then deferring the actual implementation of production code as much as possible.
- In addition to the tests described in the lab, it is recommended to also include tests for edge cases and negative scenarios to ensure robustness and reliability of the application.
- When running integration tests with a real database, it is important to properly manage the test database to avoid interfering with the production database. This can be achieved by using a separate database instance or schema for testing purposes.