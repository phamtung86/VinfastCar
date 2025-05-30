package com.vinfast.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "cars")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "car_status")
    private CarStatus carStatus;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @Fetch(FetchMode.SUBSELECT)
    private List<Library> libraries;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    private Inventory inventory;


    public enum CarStatus {
        AVAILABLE,   // Xe đang có sẵn để bán
        RESERVED,    // Đã có người đặt cọc
        SOLD,        // Đã bán
        DELIVERED    // Đã giao xe cho khách
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

    public CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}

