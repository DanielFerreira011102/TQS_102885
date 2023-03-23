package tqsdemo.carsservice.entities;

import javax.persistence.*;

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

}
