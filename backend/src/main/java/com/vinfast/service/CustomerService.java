package com.vinfast.service;

import com.vinfast.dto.CustomerDTO;
import com.vinfast.entity.Customer;
import com.vinfast.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();

        // Lọc chỉ các khách hàng có role == "customer"
        List<Customer> users = customers.stream()
                .filter(c -> "customer".equals(c.getRole()))
                .toList();

        return modelMapper.map(users, new TypeToken<List<CustomerDTO>>(){}.getType());
    }
}
