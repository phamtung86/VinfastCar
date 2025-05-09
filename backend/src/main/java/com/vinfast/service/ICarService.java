package com.vinfast.service;

import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.entity.Car;
import com.vinfast.form.CreateCarForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICarService {

    List<CarDTO> getAllCars();

    List<CarDTO> findCarsByCarStatus(Car.CarStatus carStatus);

    List<CarDTO> searchCarsByName(String name);

    boolean deleteCarById(int id);

    CarDTO getCarById(int id);

    Page<Car> getCarsByPage(Pageable pageable);

    void createNewCar(CreateCarForm createCarForm);

    boolean updateCar(CarDTO carDTO);

}
