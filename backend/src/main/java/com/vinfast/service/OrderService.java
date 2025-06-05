package com.vinfast.service;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderChartDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.entity.Car;
import com.vinfast.entity.Customer;
import com.vinfast.entity.Order;
import com.vinfast.form.CreateCarForm;
import com.vinfast.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ICustomerService customerService;

    @Override
    public List<OrderDTO> getAllOrder() {
        List<Order> orders = orderRepository.findAllOrdersWithDetails();
        return modelMapper.map(orders, new TypeToken<List<OrderDTO>>(){}.getType());
    }

    @Override
    public List<OrderDTO> searchByIdCustomerOrName(String id) {
        return List.of();
    }

    @Override
    public boolean deleteOrderById(Long id) {
        if(orderRepository.existsById(id)){
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order!=null){
            return modelMapper.map(order, new TypeToken<OrderDTO>(){}.getType());
        }
        return null;
    }

    @Override
    public Page<Order> getOrderByPage(Pageable pageable) {
        return orderRepository.findAllDistinct(pageable);
    }

    @Transactional
    @Override
    public void createNewOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        Customer customer = customerService.findCustomerById((long)orderDTO.getCustomerId());
        order.setId(null);
        if(customer!=null){
            System.out.println(order.getCar().getId());
            order.setCustomer(customer);
            orderRepository.save(order);
        }
    }
    public List<OrderChartDTO> getOrderCountByDate() {
        List<Object[]> rawData = orderRepository.getOrderCountByDateInCurrentMonth();
        List<OrderChartDTO> result = new ArrayList<>();

        for (Object[] row : rawData) {
            String date = row[0].toString();
            Long count = (Long) row[1];
            result.add(new OrderChartDTO(date, count));
        }

        return result;
    }


    @Override
    public boolean updateOrder(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId()).orElse(null);
        if(order!=null){
            order = modelMapper.map(orderDTO, Order.class);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    @Override
    public Long getRevenue() {
        Long revenue = 0L;
        List<Order> orders = orderRepository.findAllOrdersWithDetails();
        for (Order order : orders) {
            if(order.getStatus().equals("Completed")){
                revenue += order.getTotalAmount();
            }
        }
        return revenue;
    }

    @Override
    public List<OrderChartDTO> getOrderbyStatus() {
        List<Object[]> results = orderRepository.countOrdersByStatus();
        return results.stream()
                .map(obj -> new OrderChartDTO((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());
    }
    @Override
    public void updateOrderStatus(Long id, String newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id));

        // Cắt khoảng trắng đầu/cuối chuỗi
        newStatus = newStatus.trim();

        // Kiểm tra trạng thái hợp lệ
        List<String> allowedStatuses = List.of("Pending", "Processing", "Completed");
        if (!allowedStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + newStatus);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> searchOrdersByCustomerName(String name) {
        List<Order> orders = orderRepository.findByCustomerNameContainingIgnoreCase(name);
        return modelMapper.map(orders, new TypeToken<List<OrderDTO>>(){}.getType());
    }

}
