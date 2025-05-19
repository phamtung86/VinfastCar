package com.vinfast.controller;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderChartDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.entity.Car;
import com.vinfast.entity.Order;
import com.vinfast.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<OrderDTO> getAllOrder() {
        return orderService.getAllOrder();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable(name = "id") Long id) {
        boolean isDelete = orderService.deleteOrderById(id);
        return isDelete ? ResponseEntity.status(200).body("Delete success") : ResponseEntity.status(500).build();
    }
    @PutMapping
    public ResponseEntity<?> updateOrderById( @RequestBody OrderDTO dto) {
        boolean isUpdate = orderService.updateOrder(dto);
        return isUpdate ? ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }
    @GetMapping("id/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable(name = "id") Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return (orderDTO != null) ? ResponseEntity.ok(orderDTO) : ResponseEntity.notFound().build();
    }
    @GetMapping("/page")
    public ResponseEntity<Page<OrderDTO>> getCarsByPage(Pageable pageable) {
        Page<Order> pageOrder = orderService.getOrderByPage(pageable);
        List<OrderDTO> orderDTOS = modelMapper.map(pageOrder.getContent(), new TypeToken<List<OrderDTO>>() {}.getType());
        Page<OrderDTO> pageOrderDTOS = new PageImpl<>(orderDTOS, pageable, pageOrder.getTotalElements());
        return ResponseEntity.ok(pageOrderDTOS);
    }
    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody OrderDTO order) {
        try {
            orderService.createNewOrder(order);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(500).build();
    }
    @GetMapping("/chart-data")
    public List<OrderChartDTO> getOrderChartData() {
        return orderService.getOrderCountByDate();
    }

}

