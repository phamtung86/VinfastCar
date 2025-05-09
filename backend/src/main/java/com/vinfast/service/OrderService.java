package com.vinfast.service;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.entity.Order;
import com.vinfast.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<OrderDTO> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            System.out.println(order.getCustomer().getName());
        }
        return modelMapper.map(orders, new TypeToken<List<OrderDTO>>(){}.getType());
    }

    @Override
    public List<OrderDTO> searchByIdCustomerOrName(String id) {
        return List.of();
    }

    @Override
    public Boolean addOrder() {
        return null;
    }
}
