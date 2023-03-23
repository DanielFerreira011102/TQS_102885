package tqsdemo.carsservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqsdemo.carsservice.entities.Car;
import tqsdemo.carsservice.repositories.CarRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Run as a SpringBoot test. The parameters to SpringBootTest could be omitted, but, in this case,
 * we are trying to limit the web context to a simplified web framework, and load the designated application
 */
//@SpringBootTest

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = CarsServiceApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
// adapt AutoConfigureTestDatabase with TestPropertySource to use a real database
// @TestPropertySource(locations = "application-integrationtest.properties")
class D_CarRestControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void resetDb() {
        carRepository.deleteAll();
    }

    @Test
     void whenValidInput_thenCreateCar() throws Exception {
        Car car1 = new Car("model1", "maker1");
        mvc.perform(post("/api/v1/cars").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car1)));

        List<Car> found = carRepository.findAll();
        assertThat(found).extracting(Car::getModel).containsOnly("model1");
    }

    @Test
     void givenCars_whenGetCars_thenStatus200() throws Exception {
        createTestCar("model1", "maker1");
        createTestCar("model2", "maker2");

        mvc.perform(get("/api/v1/cars").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].model", is("model1")))
                .andExpect(jsonPath("$[1].model", is("model2")));
    }

    private void createTestCar(String model, String maker) {
        Car car = new Car(model, maker);
        carRepository.saveAndFlush(car);
    }


}
