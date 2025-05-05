package com.vinfast.controller;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
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

    @GetMapping("/status")
    public ResponseEntity<List<CarDTO>> getCarsByStatus(@RequestParam(name = "status") String statusStr) {
        try {
            Car.CarStatus status = Car.CarStatus.valueOf(statusStr.toUpperCase());
            return ResponseEntity.ok(carService.findCarsByCarStatus(status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<CarDTO>> searchCarsByName(@RequestParam String name) {
        List<CarDTO> carDTOS = carService.searchCarsByName(name);
        return carDTOS.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(carDTOS);
    }

    @PostMapping
    public ResponseEntity<?> addCar(@RequestBody CarDTO carDTO) {
        System.out.println("Car name: " + carDTO.getName());
        System.out.println("Images: ");
        for (LibraryDTO img : carDTO.getLibraries()) {
            System.out.println("- " + img.getTitle() + ": " + img.getUrlLink());
        }

        return ResponseEntity.ok("Thêm xe thành công");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCar(@PathVariable(name = "id") int id) {
        boolean isDelete = carService.deleteCarById(id);
        return isDelete ? ResponseEntity.status(200).body("Delete success") : ResponseEntity.status(500).build();
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<?> getCarById(@PathVariable int id) {
        CarDTO carDTO = carService.getCarById(id);
        return (carDTO != null) ? ResponseEntity.ok(carDTO) : ResponseEntity.notFound().build();
    }

}
