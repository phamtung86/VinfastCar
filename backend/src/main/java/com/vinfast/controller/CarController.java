package com.vinfast.controller;

import com.vinfast.dto.CarDTO;
import com.vinfast.entity.Car;
import com.vinfast.form.CreateCarForm;
import com.vinfast.service.ICarService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
public class CarController {

    @Autowired
    private ICarService carService;
    @Autowired
    private ModelMapper modelMapper;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCar(
            @RequestPart("car") CreateCarForm createCarForm,
            @RequestPart("images") List<MultipartFile> images
    ) {
        createCarForm.setImages(images);
        carService.createNewCar(createCarForm);
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

    @GetMapping("/page")
    public ResponseEntity<Page<CarDTO>> getCarsByPage(Pageable pageable) {
        Page<Car> pageCars = carService.getCarsByPage(pageable);
        List<CarDTO> carDTOS = modelMapper.map(pageCars.getContent(), new TypeToken<List<CarDTO>>() {}.getType());
        Page<CarDTO> pageCarDTOS = new PageImpl<>(carDTOS, pageable, pageCars.getTotalElements());
        return ResponseEntity.ok(pageCarDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarById(@PathVariable int id) {
        boolean isDelete = carService.deleteCarById(id);
        return isDelete ? ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }

    @PutMapping
    public ResponseEntity<?> updateCarById( @RequestBody CarDTO carDTO) {
        boolean isUpdate = carService.updateCar(carDTO);
        return isUpdate ? ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable(name = "id") Integer id) {
        CarDTO carDTO = carService.getCarById(id);
        return (carDTO != null) ? ResponseEntity.ok(carDTO) : ResponseEntity.notFound().build();
    }
}
