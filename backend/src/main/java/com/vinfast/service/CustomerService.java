package com.vinfast.service;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.entity.Car;
import com.vinfast.entity.Customer;
import com.vinfast.entity.Order;

import com.vinfast.entity.Customer;

import com.vinfast.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::convertToCustomerDTO).collect(Collectors.toList());
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        if (customer == null) return null;
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setCreatedAt(customer.getCreatedAt());
        customerDTO.setRole(customer.getRole());

        if (customer.getOrders() != null) {
            List<OrderDTO> orderDTOs = customer.getOrders().stream()
                    .map(this::convertToOrderDTO)
                    .collect(Collectors.toList());
            customerDTO.setOrders(orderDTOs);
        }

        return customerDTO;
    }

    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        if (order != null){
            orderDTO.setId(order.getId());
            orderDTO.setTotalAmount(order.getTotalAmount());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setPaymentMethod(order.getPaymentMethod());
            if(order.getCar() != null){
                orderDTO.setCar(convertToCarDTO(order.getCar()));
            }
        }
        return orderDTO;
    }

    private CarDTO convertToCarDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        if (car != null) {
            carDTO.setId(car.getId());
            carDTO.setName(car.getName());
            carDTO.setPrice(car.getPrice());
            carDTO.setYear(car.getYear());
            carDTO.setSlotDoor(car.getSlotDoor());
            carDTO.setSlotSeats(car.getSlotSeats());
            carDTO.setStyle(car.getStyle());
            carDTO.setOdo(car.getOdo());
        }
        return carDTO;
    }
    public Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }
}
