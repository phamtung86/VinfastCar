package com.vinfast.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryTopDTO {
    private String name;
    private int id;
    private int capacity;

    @JsonProperty("carCount")
    private int carCount;

    // Constructors
    public InventoryTopDTO() {}

    public InventoryTopDTO(String name, int id, int capacity, int carCount) {
        this.name = name;
        this.id = id;
        this.capacity = capacity;
        this.carCount = carCount;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }
}
