package com.vinfast.repository;

import com.vinfast.entity.Order;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.customer c JOIN FETCH o.car ca")
    List<Order> findAllOrdersWithDetails();
    @Query("SELECT FUNCTION('DATE', o.orderDate), COUNT(o) " +
            "FROM Order o GROUP BY FUNCTION('DATE', o.orderDate) ORDER BY FUNCTION('DATE', o.orderDate)")
    List<Object[]> getOrderCountByDate();

}
