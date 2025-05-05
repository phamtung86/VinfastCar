package com.vinfast.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany( mappedBy = "inventory",fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Car> cars;

    private int capacity;

    private String location;

    public Inventory() {
    }


}
