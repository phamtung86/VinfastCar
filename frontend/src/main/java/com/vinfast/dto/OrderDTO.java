package com.vinfast.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDTO {
    private Long id;
    private Integer customerId;
    private String customerName;
    private CarDTO car;
    private Date orderDate;
    private Long totalAmount;
    private String paymentMethod;
    private String status;
    @JsonCreator
    public OrderDTO(
            @JsonProperty("status") String status,@JsonProperty("paymentMethod") String paymentMethod,@JsonProperty("totalAmount") Long totalAmount,@JsonProperty("orderDate") Date orderDate,@JsonProperty("car") CarDTO car,@JsonProperty("customerId") Integer customerId,@JsonProperty("customerName") String customerName,@JsonProperty("id") Long id)
    {
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.car = car;
        this.customerId = customerId;
        this.customerName = customerName;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerID) {
        this.customerId = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
