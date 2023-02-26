# Exercise 2: Geocoding
This exercise involves creating an application to perform reverse geocoding and set up tests using JUnit and Mockito. The `AddressResolver` class is responsible for invoking a remote geocoding service to find a zip code for a given set of GPS coordinates. The exercise requires the implementation of unit tests to verify the behavior of `AddressResolver#findAddressForLocation`.

## Key Takeaways
- When testing a class that depends on a remote service, use a mock to simulate the behavior of the remote service and ensure consistent, predictable results.
- Use the `@Mock` and `@ExtendWith` annotations to configure JUnit and Mockito in your tests.
- Use the `when().thenReturn()` method to set up expectations for the behavior of your mocks.
- To test a method that depends on a remote service, you will need to mimic the exact (JSON) response of the remote service for a request.
- When testing with mocks, consider testing alternative cases, such as bad inputs, to ensure that the class behaves as expected.
- When running integration tests, use a real implementation of the module rather than mocks.
- To run integration tests, use the `failsafe` maven plugin and configure it appropriately.

## Answer for point (b)
- The subject under test (SuT) is the `AddressResolver` class, since it's the class we want to test the behavior of.
- The service to mock is the `ISimpleHttpClient` interface, since it's the interface responsible for making HTTP requests to the remote geocoding service, and we want to isolate the `AddressResolver` class from the remote service during testing.

# Exercise 3: Integration

## Key Takeaways
- Integration tests involve using the real implementation of a module rather than mocks.
- Use the `failsafe` maven plugin to run integration tests.
- Copy the tests from the unit test class into the integration test class and remove any support for mocking.