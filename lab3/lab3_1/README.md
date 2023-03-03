# Exercise 1: Employee manager example

This is an example of a simplified Employee management application developed using Spring Boot. The project is structured into several components such as `Employee`, `EmployeeRepository`, `EmployeeService`, and `EmployeeRestController`. The application also includes a set of tests.

## Review Questions

### a) Identify a couple of examples that use AssertJ expressive methods chaining.
AssertJ is a popular Java testing library that provides a rich set of assertion methods for testing. Expressive method chaining is a technique used to improve readability and conciseness of test code. Here are a couple of examples that use AssertJ expressive methods chaining:

1. `assertThat(list).hasSize(3).contains("foo").doesNotContain("bar")`: This example asserts that the given list has a size of 3, contains "foo" and does not contain "bar".

2. `assertThat(car).extracting(Car::getModel, Car::getMaker).containsExactly("M5", "BMW")`: This example asserts that the given car object has a model of "M5" and a maker of "BMW" using the extracting method to extract the model and maker properties of the car object.
 
### b) Identify an example in which you mock the behavior of the repository (and avoid involving a database).

Mocking the behavior of the repository is a common technique used in testing to isolate the behavior of a particular component. Here is an example of mocking the behavior of a repository on a test that verifies the business logic associated with a service implementation:

```java
@ExtendWith(MockitoExtension.class)
public class MyServiceTest {

  @Mock
  private MyRepository myRepository;

  @InjectMocks
  private MyService myService;

  @Test
  public void testDoSomething() {
    // Arrange
    when(myRepository.findById(1L)).thenReturn(Optional.of(new MyEntity(1L, "foo")));

    // Act
    myService.doSomething(1L);

    // Assert
    verify(myRepository, times(1)).findById(1L);
  }
}
```

Relying only in JUnit + Mockito makes the test a unit test, much faster that using a full SpringBootTest. No database involved.

### c) What is the difference between standard @Mock and @MockBean?

The main difference between `@Mock` and `@MockBean` is that `@Mock` is used for creating mock objects for unit testing, while `@MockBean` is used for creating mock objects for integration testing in Spring Boot applications.

`@Mock` is part of the Mockito library and is used to create mock objects that simulate the behavior of real objects. It is typically used in unit testing to isolate the behavior of a single class or method.

On the other hand, `@MockBean` is part of the Spring Boot Testing framework and is used to create mock objects for integration testing. It creates a mock object that is managed by the Spring context and can be injected into other Spring components.

In short, `@Mock` is for unit testing and `@MockBean` is for integration testing in Spring Boot applications.
 
### d) What is the role of the file “application-integrationtest.properties”? In which conditions will it be used?
The `application-integrationtest.properties` file is used to specify configuration properties for integration testing in Spring Boot applications, and is used when running integration tests with the `@SpringBootTest` annotation.

It allows us to override default configuration properties for the Spring context during integration testing, such as database connection settings and server ports.
 
### e) The sample project demonstrates three test strategies to assess an API (C, D and E) developed with SpringBoot. Which are the main/key differences?

The main differences between the three test strategies (<b>C</b>, <b>D</b>, and <b>E</b>) are:

- <b>C</b>: Only verifies the controller behavior using `@WebMvcTest`, with `@MockBean` for the service layer. It runs the tests in a simplified and light environment, simulating the behavior of an application server, and uses the `MockMvc` object to provide an entry point to server-side testing. No database is involved.

- <b>D</b>: Verifies the controller behavior with the full Spring Boot application loaded using `@SpringBootTest`, with Web Environment enabled, and the `MockMvc` object to access the server context. It tests the REST endpoint, service implementation, repository, and database. 

- <b>E</b>: Also verifies the controller behavior with the full Spring Boot application loaded, but tests the REST API using an explicit HTTP client with `TestRestTemplate` to create realistic requests. It tests the request and response un/marshalling.

Note that both <b>D</b> and <b>E</b> load the full Spring Boot application, but <b>D</b> accesses the server context through a special testing servlet (`MockMvc` object), while <b>E</b> uses an API client (`TestRestTemplate`) to test the REST API.

In short, the main differences between the three test strategies are the components being tested, the approach used to access the server context, and the tool used to make requests to the REST API.

## Key Takeaways
- Spring Boot provides a convenient way to structure your application into multiple components such as entities, repositories, services, and REST controllers.
- JpaRepository provides a set of methods to perform common CRUD operations on the target entity. Custom queries can also be defined if needed.
- `@Service` annotation indicates that a class is a service component that contains business logic related to the application.
- `@RestController` annotation indicates that a class is a REST controller that handles HTTP requests and delegates them to appropriate service components.
- AssertJ provides expressive methods chaining to write more readable and concise assertions in your test cases.