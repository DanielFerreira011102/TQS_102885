package me.carsservice.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tqs_car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    private String model;

    private String maker;

    // default constructor
    public Car() {}

    // parameterized constructor
    public Car(String model, String maker) {
        this.model = model;
        this.maker = maker;
    }

    // getters and setters
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    // toString method
    @Override
    public String toString() {
        return "Car [carId=" + carId + ", model=" + model + ", maker=" + maker + "]";
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (!Objects.equals(carId, car.carId)) return false;
        if (!Objects.equals(model, car.model)) return false;
        return Objects.equals(maker, car.maker);
    }

    // hashCode method
    @Override
    public int hashCode() {
        int result = carId != null ? carId.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (maker != null ? maker.hashCode() : 0);
        return result;
    }

}
