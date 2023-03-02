package me.carsservice.services;

import me.carsservice.entities.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.carsservice.repositories.CarRepository;
import java.util.List;

@Service
public class CarManagerServiceImpl implements CarManagerService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public Car getCarDetails(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

}
