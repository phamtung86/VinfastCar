package com.vinfast.dto;

import com.vinfast.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CarDTO {

    private Integer id;

    private String name;

    private int year;

    private String status;

    private double odo;

    private String original;

    private String style;

    private String gear;

    private String engine;

    private String colorOut;

    private String colorIn;

    private int slotSeats;

    private int slotDoor;

    private String driveTrain;

    private long price;

    private Car.CarStatus carStatus;

    private List<InventoryDTO> inventories;

    private  List<LibraryDTO> libraries;

    public CarDTO() {
    }

    public CarDTO(Integer id, String name, int year, String status, double odo, String original, String style, String gear, String engine, String colorOut, String colorIn, int slotSeats, int slotDoor, String driveTrain, long price, Car.CarStatus carStatus, List<InventoryDTO> inventories, List<LibraryDTO> libraries) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.status = status;
        this.odo = odo;
        this.original = original;
        this.style = style;
        this.gear = gear;
        this.engine = engine;
        this.colorOut = colorOut;
        this.colorIn = colorIn;
        this.slotSeats = slotSeats;
        this.slotDoor = slotDoor;
        this.driveTrain = driveTrain;
        this.price = price;
        this.carStatus = carStatus;
        this.inventories = inventories;
        this.libraries = libraries;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getOdo() {
        return odo;
    }

    public void setOdo(double odo) {
        this.odo = odo;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getColorOut() {
        return colorOut;
    }

    public void setColorOut(String colorOut) {
        this.colorOut = colorOut;
    }

    public String getColorIn() {
        return colorIn;
    }

    public void setColorIn(String colorIn) {
        this.colorIn = colorIn;
    }

    public int getSlotSeats() {
        return slotSeats;
    }

    public void setSlotSeats(int slotSeats) {
        this.slotSeats = slotSeats;
    }

    public int getSlotDoor() {
        return slotDoor;
    }

    public void setSlotDoor(int slotDoor) {
        this.slotDoor = slotDoor;
    }

    public String getDriveTrain() {
        return driveTrain;
    }

    public void setDriveTrain(String driveTrain) {
        this.driveTrain = driveTrain;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public List<InventoryDTO> getInventories() {
        return inventories;
    }

    public void setInventories(List<InventoryDTO> inventories) {
        this.inventories = inventories;
    }

    public List<LibraryDTO> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<LibraryDTO> libraries) {
        this.libraries = libraries;
    }

    public Car.CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Car.CarStatus carStatus) {
        this.carStatus = carStatus;
    }
}
