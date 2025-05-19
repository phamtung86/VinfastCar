package com.vinfast.service;

import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.entity.Car;
import com.vinfast.entity.Customer;
import com.vinfast.entity.Order;
import com.vinfast.form.ConvertDTO;
import com.vinfast.repository.CarRepository;
import com.vinfast.repository.CustomerRepository;
import com.vinfast.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ConvertDTO convertDTO;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(convertDTO::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    @Override
    public Page<CustomerDTO> getAllCustomersToPage(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(convertDTO::convertToCustomerDTO);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> convertDTO.convertToCustomerDTO(customer))  // Sử dụng phương thức convertToCustomerDTO để chuyển đổi
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setCreatedAt(new Date());
        customer.setRole("customer");

        if (customerDTO.getOrders() != null && !customerDTO.getOrders().isEmpty()) {
            List<Order> orders = customerDTO.getOrders().stream().map(orderDTO -> {
                Order order = new Order();
                order.setOrderDate(new Date());
                order.setPaymentMethod(orderDTO.getPaymentMethod());
//                order.setTotalAmount(orderDTO.getTotalAmount());
                order.setStatus(orderDTO.getStatus());
                order.setCustomer(customer); // Gán ngược lại khách hàng

                if (orderDTO.getCar() != null) {
                    Integer carId = orderDTO.getCar().getId(); // Không cần ép kiểu
                    Car car = carRepository.findById(carId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + carId));
                    order.setCar(car);
                    order.setTotalAmount(car.getPrice()); // Gán totalAmount theo giá xe
                }

                return order;
            }).collect(Collectors.toList());

            customer.setOrders(orders);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return convertDTO.convertToCustomerDTO(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng"));

        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());

        Customer saved = customerRepository.save(customer);
        return convertDTO.convertToCustomerDTO(saved);
    }

    @Override
    public boolean deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        customerRepository.delete(customer);
        return true;
    }

    @Override
    public CustomerDTO addOrderToCustomer(Long id, OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));

        Order order = new Order();
        order.setOrderDate(new Date());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setStatus(orderDTO.getStatus());
        order.setCustomer(customer);

        if (orderDTO.getCar() != null) {
            Integer carId = orderDTO.getCar().getId();
            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy xe với ID: " + carId));
            order.setCar(car);
            order.setTotalAmount(car.getPrice());
        }

        customer.getOrders().add(order);

        Customer savedCustomer = customerRepository.save(customer);
        return convertDTO.convertToCustomerDTO(savedCustomer);
    }

    @Override
    public  CustomerDTO updateOrderStatus(Long customerId, Long orderId, String newStatus) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + customerId));

        Optional<Order> optionalOrder = customer.getOrders()
                .stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();

        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng của khách hàng");
        }

        Order order = optionalOrder.get();
        order.setStatus(newStatus);

        Customer savedCustomer = customerRepository.save(customer);
        return convertDTO.convertToCustomerDTO(savedCustomer);
    }

    @Override
    public CustomerDTO deleteOrder(Long customerId, Long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + customerId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Đơn hàng không thuộc về khách hàng này");
        }

        orderRepository.delete(order);

        return convertDTO.convertToCustomerDTO(customer);
    }

    public boolean emailExists(String email) {
        return customerRepository.existsByEmail(email);
    }

    public boolean phoneExists(String phone) {
        return customerRepository.existsByPhone(phone);
    }
}