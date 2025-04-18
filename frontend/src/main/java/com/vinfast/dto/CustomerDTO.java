package com.vinfast.dto;


import java.util.Date;
import java.util.List;

public class CustomerDTO {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Date createdAt;
    private List<OrderDTO> orders;
}
