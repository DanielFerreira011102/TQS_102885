package tqsdemo.carsservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import tqsdemo.carsservice.entities.Car;
import tqsdemo.carsservice.repositories.CarRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test scenario: verify the logic of the Service, mocking the response of the datasource
 * Results in standard unit test with mocks
 */
@ExtendWith(MockitoExtension.class)
class B_CarService_UnitTest {

    // mocking the responses of the repository (i.e., no database will be used)
    // lenient is required because we load more expectations in the setup
    // than those used in some tests. As an alternative, the expectations
    // could move into each test method and be trimmed (no need for lenient then)
    @Mock(lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerServiceImpl carService;

    @BeforeEach
    public void setUp() {

        //these expectations provide an alternative to the use of the repository
        Car car1 = new Car("model1", "maker1");
        car1.setCarId(111L);

        Car car2 = new Car("model2", "maker2");
        Car car3 = new Car("model3", "maker3");

        List<Car> allCars = Arrays.asList(car1, car2, car3);

        Mockito.when(carRepository.findById(car1.getCarId())).thenReturn(Optional.of(car1));
        Mockito.when(carRepository.findAll()).thenReturn(allCars);
        Mockito.when(carRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
     void whenSearchValidId_thenCarShouldBeFound() {
        Long id = 111L;
        Car found = carService.getCarDetails(id);

        verifyFindByIdIsCalledOnce();

        assertThat(found.getCarId()).isEqualTo(id);
        assertThat(found.getModel()).isEqualTo("model1");
        assertThat(found.getMaker()).isEqualTo("maker1");
    }

    @Test
     void whenSearchInvalidId_thenCarShouldNotBeFound() {
        Car fromDb = carService.getCarDetails(-99L);

        verifyFindByIdIsCalledOnce();

        assertThat(fromDb).isNull();
    }

    @Test
     void given3Cars_whengetAll_thenReturn3Records() {
        Car car1 = new Car("model1", "maker1");
        Car car2 = new Car("model2", "maker2");
        Car car3 = new Car("model3", "maker3");

        List<Car> allCars = carService.getAllCars();

        verifyFindAllCarsIsCalledOnce();

        assertThat(allCars).hasSize(3).extracting(Car::getModel).contains(car1.getModel(), car2.getModel(), car3.getModel());
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
    }

    private void verifyFindAllCarsIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findAll();
    }
}
