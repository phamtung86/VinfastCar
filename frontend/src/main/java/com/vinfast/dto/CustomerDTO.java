package com.vinfast.dto;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CustomerDTO {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Date createdAt;
    private List<OrderDTO> orders;

    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String name, String phone, String email, String address, Date createdAt, List<OrderDTO> orders) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdAt = createdAt;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public long getTotalAmount() {
        if (orders == null || orders.isEmpty()) return 0L;
        return orders.stream()
                .filter(Objects::nonNull)
                .mapToLong(OrderDTO::getTotalAmount)
                .sum();
    }
}
