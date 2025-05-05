package com.vinfast.repository;

import com.vinfast.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT c FROM Car c WHERE c.carStatus = :status")
    List<Car> findByCarStatus(@Param("status") Car.CarStatus carStatus);

    List<Car> findByNameContainingIgnoreCase(String name);


}
