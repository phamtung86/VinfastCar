package com.vinfast.service;

import com.vinfast.dto.CustomerDTO;

import java.util.List;
import com.vinfast.entity.Customer;
public interface ICustomerService {
    List<CustomerDTO> getAllCustomers();


        Customer findCustomerById(Long customerId);
}
