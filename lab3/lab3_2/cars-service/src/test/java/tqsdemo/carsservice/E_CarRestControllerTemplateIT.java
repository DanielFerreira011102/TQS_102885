package tqsdemo.carsservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqsdemo.carsservice.entities.Car;
import tqsdemo.carsservice.repositories.CarRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase

// switch AutoConfigureTestDatabase with TestPropertySource to use a real database
//@TestPropertySource(locations = "application-integrationtest.properties")
class E_CarRestControllerTemplateIT {

    // will need to use the server port for the invocation url
    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void resetDb() {
        carRepository.deleteAll();
    }


    @Test
     void whenValidInput_thenCreateCar() {
        Car car1 = new Car("model1", "maker1");
        ResponseEntity<Car> response = restTemplate.postForEntity("/api/v1/cars", car1, Car.class);

        HttpStatus status = response.getStatusCode();
        Car created = response.getBody();

        assertThat(status).isEqualTo(HttpStatus.CREATED);
        assertThat(created).isNotNull();
        assertThat(created.getModel()).isEqualTo("model1");

        List<Car> found = carRepository.findAll();
        assertThat(found).extracting(Car::getModel).containsOnly("model1");
    }

    @Test
     void givenCars_whenGetEmployees_thenStatus200()  {
        createTestCar("model1", "maker1");
        createTestCar("model2", "maker2");


        ResponseEntity<List<Car>> response = restTemplate
                .exchange("/api/v1/cars", HttpMethod.GET, null, new ParameterizedTypeReference<List<Car>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Car::getModel).containsExactly("model1", "model2");

    }


    private void createTestCar(String model, String maker) {
        Car car = new Car(model, maker);
        carRepository.saveAndFlush(car);
    }

}
