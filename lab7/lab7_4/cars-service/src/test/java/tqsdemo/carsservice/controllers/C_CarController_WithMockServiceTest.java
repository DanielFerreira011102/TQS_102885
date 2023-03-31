package tqsdemo.carsservice.controllers;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tqsdemo.carsservice.JsonUtils;
import tqsdemo.carsservice.entities.Car;
import tqsdemo.carsservice.repositories.CarRepository;

import java.io.IOException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create") // note the TestPropertySource to enforce the ddl generation!
class C_CarController_WithMockServiceTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest")
            .withUsername("demo")
            .withPassword("demopass")
            .withDatabaseName("test_db");

    @LocalServerPort
    int localPortForTestServer;
    Car car1, car2;

    @Autowired
    private CarRepository repository;

    // read configuration from running db
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);

    }

    @BeforeEach
    public void setUpTestCars() {
        repository.flush();
        car1 = repository.save(new Car("stinger", "kia"));
        car2 = repository.save(new Car("model x", "tesla"));
    }

    @AfterEach
    public void resetCars() {
        repository.deleteAll();
    }

    @Test
    void whenPostCar_thenCreateCar() throws IOException {
        Car car1 = new Car("E 220 Cabrio", "Mercedes-Benz");

        String endpoint = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(localPortForTestServer)
                .pathSegment("api", "v1", "cars")
                .build()
                .toUriString();

        RestAssured.given().auth().none().contentType("application/json").body(JsonUtils.toJson(car1))
                .when().post(endpoint)
                .then().statusCode(201)
                .body("model", is("E 220 Cabrio"))
                .body("maker", is("Mercedes-Benz"));
    }

    @Test
    void whenGetCarById_thenApiReturnsOneCar() {
        String endpoint = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(localPortForTestServer)
                .pathSegment("api", "v1", "cars", String.valueOf( car1.getCarId()) )
                .build()
                .toUriString();

        RestAssured.given().auth().none().contentType("application/json").get(endpoint)
                .then().statusCode(200)
                .body("model", is("stinger"))
                .body("maker", is("kia"));
    }

    @Test
    void givenManyCars_whenGetCars_thenReturnJsonArray() {
        String endpoint = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(localPortForTestServer)
                .pathSegment("api", "v1", "cars")
                .build()
                .toUriString();

        RestAssured.given().auth().none().contentType("application/json").get(endpoint)
                .then().statusCode(200)
                .body("", hasSize(2))
                .body("[0].model", is(car1.getModel()))
                .body("[1].model", is(car2.getModel()))
                .body("[0].maker", is(car1.getMaker()))
                .body("[1].maker", is(car2.getMaker()));
    }
}
