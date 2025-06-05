package com.vinfast.repository;

import com.vinfast.entity.Car;
import com.vinfast.entity.Customer;
import com.vinfast.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.customer c JOIN FETCH o.car ca")
    List<Order> findAllOrdersWithDetails();
    @Query("SELECT FUNCTION('DATE', o.orderDate), COUNT(o) " +
            "FROM Order o " +
            "WHERE FUNCTION('MONTH', o.orderDate) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY FUNCTION('DATE', o.orderDate) " +
            "ORDER BY FUNCTION('DATE', o.orderDate)")
    List<Object[]> getOrderCountByDateInCurrentMonth();
    @Query("SELECT DISTINCT c FROM Order c")
    Page<Order> findAllDistinct(Pageable pageable);
    @Query("SELECT o.status, COUNT(o) " +
            "FROM Order o " +
            "GROUP BY o.status")
    List<Object[]> countOrdersByStatus();
    List<Order> findByCustomerNameContainingIgnoreCase(String name);
}
