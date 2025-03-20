package com.vinfast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private CustomerDTO customer;
    private CarDTO car;
    private Date orderDate;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;

}
