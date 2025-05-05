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

    @Override
    public List<CarDTO> searchCarsByName(String name) {
        List<Car> cars = carRepository.findByNameContainingIgnoreCase(name);
        List<CarDTO> carDTOS = modelMapper.map(cars, new TypeToken<List<CarDTO>>(){}.getType());
        return carDTOS;
    }

    @Override
    public boolean deleteCarById(int id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public CarDTO getCarById(int id) {
        Car car = carRepository.findById(id).orElse(null);
        if(car != null) {
            return modelMapper.map(car, new TypeToken<CarDTO>(){}.getType());
        }
        return null;
    }

}
