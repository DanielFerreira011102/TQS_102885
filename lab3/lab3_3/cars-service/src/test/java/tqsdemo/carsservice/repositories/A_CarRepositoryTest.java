package tqsdemo.carsservice.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqsdemo.carsservice.entities.Car;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DataJpaTest limits the test scope to the data access context (no web environment loaded, for example)
 * tries to autoconfigure the database, if possible (e.g.: in memory db)
 */
@DataJpaTest
class A_CarRepositoryTest {

    // get a test-friendly Entity Manager
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    void whenFindCarById_thenReturnCar() {
        // arrange a new car and insert into db
        Car car1 = new Car("model1", "maker1");
        entityManager.persistAndFlush(car1); //ensure data is persisted at this point

        // test the query method of interest
        Car found = carRepository.findById(car1.getCarId()).orElse(null);

        assertThat(found).isNotNull();

        assertThat(found.getModel()).isEqualTo(car1.getModel());
        assertThat(found.getMaker()).isEqualTo(car1.getMaker());
    }

    @Test
    void whenInvalidCarId_thenReturnNull() {
        Car fromDb = carRepository.findById(-99L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfCars_whenFindAll_thenReturnAllCars() {
        Car car1 = new Car("model1", "maker1");
        Car car2 = new Car("model2", "maker2");
        Car car3 = new Car("model3", "maker3");

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);
        entityManager.flush();

        List<Car> allCars = carRepository.findAll();

        assertThat(allCars).hasSize(3).extracting(Car::getModel).contains(car1.getModel(), car2.getModel(), car3.getModel());
    }

}