package com.vinfast.controller;

import com.vinfast.dto.CarDTO;
import com.vinfast.entity.Car;
import com.vinfast.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
public class CarController {

    @Autowired
    private ICarService carService;

    @GetMapping
    public List<CarDTO> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/car-status")
    public ResponseEntity<List<CarDTO>> getCarsByStatus(@RequestParam(name = "status") String statusStr) {
        Car.CarStatus status = Car.CarStatus.valueOf(statusStr.toUpperCase());
        if (status.equals(Car.CarStatus.RESERVED) || status.equals(Car.CarStatus.AVAILABLE) || status.equals(Car.CarStatus.SOLD) || status.equals(Car.CarStatus.DELIVERED)) {
            List<CarDTO> carDTOS = carService.findCarsByCarStatus(status);
            return ResponseEntity.ok(carDTOS);
        }
        return ResponseEntity.notFound().build();
    }

}
