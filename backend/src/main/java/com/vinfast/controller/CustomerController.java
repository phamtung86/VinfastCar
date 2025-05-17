package com.vinfast.controller;

import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getCustomers() {return customerService.getAllCustomers();}

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.addCustomer(customerDTO);
        return ResponseEntity.ok("Thêm thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(id, customerDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cập nhật khách hàng thành công");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        boolean isDeleted = customerService.deleteCustomer(id);
        if (isDeleted) {
            return ResponseEntity.ok("Customer deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{customerId}/orders")
    public ResponseEntity<CustomerDTO> addOrderToCustomer(@PathVariable Long customerId,
                                                          @RequestBody OrderDTO orderDTO) {
        CustomerDTO updatedCustomer = customerService.addOrderToCustomer(customerId, orderDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PutMapping("/{customerId}/orders/{orderId}/status")
    public ResponseEntity<CustomerDTO> updateOrderStatusForCustomer(
            @PathVariable Long customerId,
            @PathVariable Long orderId,
            @RequestBody Map<String, String> statusMap) {

        String newStatus = statusMap.get("status");
        CustomerDTO updatedCustomer = customerService.updateOrderStatus(customerId, orderId, newStatus);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}/orders/{orderId}")
    public ResponseEntity<?> deleteOrderFromCustomer(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            CustomerDTO updatedCustomer = customerService.deleteOrder(customerId, orderId);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
