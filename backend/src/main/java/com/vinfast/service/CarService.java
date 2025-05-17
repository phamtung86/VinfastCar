package com.vinfast.service;

import com.vinfast.dto.CarDTO;
import com.vinfast.entity.Car;
import com.vinfast.form.CreateCarForm;
import com.vinfast.repository.CarRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService implements ICarService {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ILibraryService libraryService;


    @Override
    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAllWithLibrariesAndInventory();
        return modelMapper.map(cars, new TypeToken<List<CarDTO>>() {
        }.getType());
    }

    @Override
    public List<CarDTO> findCarsByCarStatus(Car.CarStatus carStatus) {
        List<Car> cars = carRepository.findByCarStatus(carStatus);
        return modelMapper.map(cars, new TypeToken<List<CarDTO>>() {
        }.getType());
    }

    @Override
    public List<CarDTO> searchCarsByName(String name) {
        List<Car> cars = carRepository.findByNameContainingIgnoreCase(name);
        List<CarDTO> carDTOS = modelMapper.map(cars, new TypeToken<List<CarDTO>>() {
        }.getType());
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
        if (car != null) {
            return modelMapper.map(car, new TypeToken<CarDTO>() {
            }.getType());
        }
        return null;
    }

    @Override
    public Page<Car> getCarsByPage(Pageable pageable) {
        return carRepository.findAllDistinct(pageable);
    }

    @Transactional
    @Override
    public void createNewCar(CreateCarForm createCarForm) {
        Car car = modelMapper.map(createCarForm, Car.class);
        car.setCarStatus(Car.CarStatus.AVAILABLE);
        Car carResponse = carRepository.save(car);
        System.out.println(carResponse.getId());
        libraryService.createLibrary(carResponse, createCarForm);
    }

    @Override
    public boolean updateCar(CarDTO carDTO) {
        Car car = carRepository.findById(carDTO.getId()).orElse(null);
        if (car != null) {
            car = modelMapper.map(carDTO, Car.class);
            carRepository.save(car);
            return true;
        }
        return false;
    }


}
