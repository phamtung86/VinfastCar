package com.vinfast.service;

import com.vinfast.dto.CarDTO;
import com.vinfast.entity.Car;

import java.util.List;

public interface ICarService {

    List<CarDTO> getAllCars();

    List<CarDTO> findCarsByCarStatus(Car.CarStatus carStatus);

    List<CarDTO> searchCarsByName(String name);

    boolean deleteCarById(int id);

    CarDTO getCarById(int id);
}
