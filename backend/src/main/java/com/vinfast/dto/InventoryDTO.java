package com.vinfast.dto;

import java.util.List;

public class InventoryDTO {

    private Long id;
    private int capacity;
    private String location;
    private String name;
    private List<CarDTO> cars;

    public InventoryDTO() {
    }

    public InventoryDTO(int capacity, String location, String name, List<CarDTO> cars, Long id) {
        this.capacity = capacity;
        this.location = location;
        this.name = name;
        this.cars = cars;
        this.id = id;
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

    public void setName(String name) {
        this.name = name;
    }
}
