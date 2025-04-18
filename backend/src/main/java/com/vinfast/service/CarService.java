package com.vinfast.service;

import com.vinfast.dto.CarDTO;
import com.vinfast.entity.Car;
import com.vinfast.repository.CarRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService implements ICarService{

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return modelMapper.map(cars, new TypeToken<List<CarDTO>>(){}.getType());
    }

    @Override
    public List<CarDTO> findCarsByCarStatus(Car.CarStatus carStatus) {
        List<Car> cars = carRepository.findByCarStatus(carStatus);
        return modelMapper.map(cars, new TypeToken<List<CarDTO>>(){}.getType());
    }


}
