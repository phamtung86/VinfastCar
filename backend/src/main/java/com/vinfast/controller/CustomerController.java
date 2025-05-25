package com.vinfast.controller;

import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/paging")
    public ResponseEntity<?> getCustomersPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CustomerDTO> customerPage = customerService.getAllCustomersToPage(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("customers", customerPage.getContent());
        response.put("currentPage", customerPage.getNumber());
        response.put("totalItems", customerPage.getTotalElements());
        response.put("totalPages", customerPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = customerService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phone) {
        boolean exists = customerService.phoneExists(phone);
        return ResponseEntity.ok(exists);
    }
}
