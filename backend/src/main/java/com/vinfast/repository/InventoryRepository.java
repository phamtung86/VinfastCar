package com.vinfast.repository;

import com.vinfast.entity.Car;
import com.vinfast.entity.Inventory;
import com.vinfast.models.IInventoryTopModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT i.id AS id, i.name AS name, i.capacity AS capacity, COUNT(c.id) AS carCount " +
            "FROM Inventory i JOIN i.cars c " +
            "GROUP BY i.id, i.name, i.capacity " +
            "ORDER BY COUNT(c.id) DESC")
    List<IInventoryTopModel> findTopInventories(Pageable pageable);

    @EntityGraph(attributePaths = {"cars"})
    List<Inventory> findAll();
}

