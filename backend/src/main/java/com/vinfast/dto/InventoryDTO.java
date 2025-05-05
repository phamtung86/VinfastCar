package com.vinfast.dto;

import java.util.List;

public class InventoryDTO {

    private Long id;
    private int Capacity;
    private String location;
    private List<CarDTO> cars;

    public InventoryDTO() {
    }

    public InventoryDTO(Long id, int capacity, String location, List<CarDTO> cars) {
        this.id = id;
        Capacity = capacity;
        this.location = location;
        this.cars = cars;
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
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public List<CarDTO> getCars() {
        return cars;
    }

    public void setCars(List<CarDTO> cars) {
        this.cars = cars;
    }
}
