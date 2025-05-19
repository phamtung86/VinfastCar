package com.vinfast.form;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.entity.Car;
import com.vinfast.entity.Customer;
import com.vinfast.entity.Library;
import com.vinfast.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvertDTO {

    public CustomerDTO convertToCustomerDTO(Customer customer) {
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

    public OrderDTO convertToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        if (order != null){
            orderDTO.setId(order.getId());
            orderDTO.setTotalAmount(order.getTotalAmount());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setPaymentMethod(order.getPaymentMethod());
            orderDTO.setStatus(order.getStatus());
            if(order.getCar() != null){
                orderDTO.setCar(convertToCarDTO(order.getCar()));
            }
        }
        return orderDTO;
    }

    public CarDTO convertToCarDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        if (car != null){
            carDTO.setId(car.getId());
            carDTO.setName(car.getName());
            carDTO.setPrice(car.getPrice());
            carDTO.setYear(car.getYear());
            carDTO.setSlotDoor(car.getSlotDoor());
            carDTO.setSlotSeats(car.getSlotSeats());
            carDTO.setStyle(car.getStyle());
            carDTO.setOdo(car.getOdo());
            carDTO.setGear(car.getGear());
            carDTO.setOriginal(car.getOriginal());
            carDTO.setEngine(car.getEngine());
            carDTO.setStatus(car.getStatus());
            carDTO.setColorIn(car.getColorIn());
            carDTO.setColorOut(car.getColorOut());
            carDTO.setDriveTrain(car.getDriveTrain());
            if (car.getLibraries() != null && !car.getLibraries().isEmpty()) {
                carDTO.setLibraries(convertToLibraryDTOList(car.getLibraries()));
            }
        }
        return carDTO;
    }

    public LibraryDTO convertToLibraryDTO(Library library) {
        LibraryDTO libraryDTO = new LibraryDTO();
        if (library != null){
            libraryDTO.setId(library.getId());
            libraryDTO.setUrlLink(library.getUrlLink());
            libraryDTO.setTitle(library.getTitle());
            libraryDTO.setCarId(library.getCar().getId());
        }
        return libraryDTO;
    }

    public List<LibraryDTO> convertToLibraryDTOList(List<Library> libraries) {
        return libraries.stream()
                .map(this::convertToLibraryDTO)
                .collect(Collectors.toList());
    }
}
