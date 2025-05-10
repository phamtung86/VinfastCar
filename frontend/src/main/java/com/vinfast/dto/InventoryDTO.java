
package com.vinfast.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InventoryDTO{

    private Long id;
    private int capacity;
    private String location;
    private String name;
    private List<CarDTO> cars;
    private int carCount;
    private float perFull;

    public InventoryDTO() {
    }
    @JsonCreator
    public InventoryDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("capacity") Integer capacity,
            @JsonProperty("location") String location,
            @JsonProperty("name") String name,
            @JsonProperty("cars") List<CarDTO> cars
    ){
        this.capacity = capacity;
        this.location = location;
        this.name = name;
        this.id = id;
        this.cars = cars;
        this.carCount = cars.size();
        this.perFull = (float) cars.size()/capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<CarDTO> getCars() {
        return cars;
    }

    public void setCars(List<CarDTO> cars) {
        this.cars = cars;
    }

    public String getName() {
        return name;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    public float getPerFull() {
        return perFull;
    }

    public void setPerFull(float perFull) {
        this.perFull = perFull;
    }

    public void setName(String name) {
        this.name = name;
    }
}
