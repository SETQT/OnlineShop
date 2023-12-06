package com.Shop.Service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Shop.Model.Order;
import com.Shop.Repository.OrderRepository;
import com.Shop.Service.Generic.GenericService;

@Service
public class OrderService extends GenericService<Order> implements IOrderService {
	public OrderService(OrderRepository orderRepository) {
		super(orderRepository);
	}

	public List<Order> findProductsByPage(int i, int size, String sortBy) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(i, size, Sort.by(sortBy));
		Page<Order> pagedResult = ((OrderRepository) genericRepository).findAll(paging);

		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Order>();
		}

	}

	public List<Order> findOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
		return ((OrderRepository) genericRepository).findOrdersBetweenDates(startDate, endDate);
	}

	public Map<String, Object> findProductsWithPaginationDetails(int pageNumber, int pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		Page<Order> pagedResult = ((OrderRepository) genericRepository).findAll(paging);
		Map<String, Object> response = new HashMap<>();
		response.put("orders", pagedResult.getContent());
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());

		if (pagedResult.hasContent()) {
			response.put("orders", pagedResult.getContent());
		} else {
			response.put("orders", new ArrayList<>());
		}

		return response;
	}

	// Cài đặt các phương thức cụ thể cho dịch vụ Order (nếu cần)
}