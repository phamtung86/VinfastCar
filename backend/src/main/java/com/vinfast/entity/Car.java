package com.vinfast.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private int year;

    private int odo;

    @Column(name = "`condition`")
    private String condition;

    private BigDecimal price;

    private String status;

    private String description;

    private String imageUrl;

    private Date createdAt;

    private Date updatedAt;

    @OneToMany(mappedBy = "car")
    private List<Inventory> inventory;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;
//
//    @OneToMany(mappedBy = "car")
//    private List<Order> orders;
}

