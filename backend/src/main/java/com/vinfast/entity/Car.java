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
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
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
}

