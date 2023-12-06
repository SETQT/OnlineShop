package com.Shop.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Shop.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
	List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

}
