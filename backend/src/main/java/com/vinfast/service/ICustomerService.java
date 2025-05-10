package com.vinfast.service;

import com.vinfast.dto.CustomerDTO;

import java.util.List;

public interface ICustomerService {

    List<CustomerDTO> getCustomers();
}
