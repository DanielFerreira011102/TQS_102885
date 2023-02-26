# Lab 2: Mocking dependencies (for unit testing)

This lab focuses on mocking dependencies for unit testing using JUnit 5 and Mockito. The following topics will be covered:

## Learning Objectives
- Prepare a project to run unit tests (JUnit 5) and mocks (Mockito), with mocks injection (@Mock).
- Write and execute unit tests with mocked dependencies.
- Experiment with mock behaviors: strict/lenient verifications, advanced verifications, etc.

## Key Points
- Mocking is a technique used to isolate dependencies in unit tests.
- Mockito is a popular Java mocking framework that simplifies the process of creating and managing mock objects.
- The `@Mock` annotation in Mockito can be used to inject mock objects into test classes.

## Mocking Best Practices
- Use mocking to isolate dependencies and ensure unit tests are focused and predictable.
- Keep mock behavior simple and specific to the test case.
- Use strict verifications for scenarios where specific interactions between objects are important.
- Use lenient verifications for scenarios where the order of interactions is not important.
- Use advanced verifications to verify more complex interactions between objects.

## Notes
- When using Mockito,  `UnnecessaryStubbingException` might occur if there is a stub that is not needed in the current test scenario.
- This exception can be avoided by configuring the Mockito settings to use lenient strictness instead of the default strictness. This can be done using the `@MockitoSettings` annotation with the `strictness` attribute set to `LENIENT`.
- However, it's important to use this feature judiciously as it may hide issues with the test code or cause unexpected behavior. It's recommended to use lenient strictness only when necessary and to ensure that all stubs are necessary and relevant to the test scenario.