package com.vinfast.dto;


import java.math.BigDecimal;
import java.util.Date;

public class OrderDTO {
    private Long id;
    private CustomerDTO customer;
    private CarDTO car;
    private Date orderDate;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;

}
