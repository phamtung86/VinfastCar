package com.vinfast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private Long id;

    private String model;

    private int year;

    private int odo;

    private String condition;

    private BigDecimal price;

    private String status;

    private String description;

    private String imageUrl;

    private Date createdAt;

    private Date updatedAt;

    private List<InventoryDTO> inventory;

    private Integer brandId;

    private String brandName;

//    private Integer orderId;
}
