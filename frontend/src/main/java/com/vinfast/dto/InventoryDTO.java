package com.vinfast.dto;

public class InventoryDTO {

    private Long id;
    private Long carId;
    private String carName;
    private int quantity;
    private String location;

    public InventoryDTO() {
    }

    public InventoryDTO(Long id, Long carId, String carName, int quantity, String location) {
        this.id = id;
        this.carId = carId;
        this.carName = carName;
        this.quantity = quantity;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
