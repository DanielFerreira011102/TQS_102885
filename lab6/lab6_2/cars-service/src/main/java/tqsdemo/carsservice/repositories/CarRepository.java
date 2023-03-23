package tqsdemo.carsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqsdemo.carsservice.entities.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    public List<Car> findAll();
}
