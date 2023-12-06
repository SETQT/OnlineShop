package com.Shop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shop.Model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
