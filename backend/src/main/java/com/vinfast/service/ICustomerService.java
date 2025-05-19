package com.vinfast.service;

import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(Long id);

    CustomerDTO addCustomer(CustomerDTO customer);

    CustomerDTO updateCustomer(Long id, CustomerDTO customer);

    boolean deleteCustomer(Long id);

    CustomerDTO addOrderToCustomer(Long id, OrderDTO order);

    CustomerDTO updateOrderStatus(Long customerId, Long orderId, String newStatus);

    CustomerDTO deleteOrder(Long customerId, Long orderId);

    Page<CustomerDTO> getAllCustomersToPage(Pageable pageable);

    boolean emailExists(String email);

    boolean phoneExists(String phone);
}
