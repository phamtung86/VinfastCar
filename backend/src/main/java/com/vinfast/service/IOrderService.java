package com.vinfast.service;

import com.vinfast.dto.OrderDTO;

import java.util.List;

public interface IOrderService {
    List<OrderDTO> getAllOrder();
    List<OrderDTO> searchByIdCustomerOrName(String id);
    Boolean addOrder();

}
