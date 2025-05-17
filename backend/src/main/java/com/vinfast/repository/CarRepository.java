package com.vinfast.repository;

import com.vinfast.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph(attributePaths = {"libraries", "inventory"})
    @Query("SELECT c FROM Car c")
    List<Car> findAllWithLibrariesAndInventory();

    @EntityGraph(attributePaths = {"libraries", "inventory"})
    @Query("SELECT DISTINCT c FROM Car c")
    Page<Car> findAllDistinct(Pageable pageable);

}
