package com.vinfast.controller;

import com.vinfast.dto.CustomerDTO;
import com.vinfast.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getCustomers() {return customerService.getAllCustomers();}
}
