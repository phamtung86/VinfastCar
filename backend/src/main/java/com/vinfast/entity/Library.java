package com.vinfast.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "library")
@Data
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String urlLink;

    private String title;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;
}
