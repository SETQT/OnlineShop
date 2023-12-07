package com.Shop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shop.Model.OrderItem;
import com.Shop.Model.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByProduct(Product product);
}
