package tqsdemo.carsservice.services;

import tqsdemo.carsservice.entities.Car;
import java.util.List;

public interface CarManagerService {
    public Car getCarDetails(Long id);
    public List<Car> getAllCars();
    public Car save(Car car);
}
