package com.Shop.Service.ProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.Shop.DTO.CateDTO;
import com.Shop.Model.Order;
import com.Shop.Model.OrderItem;
import com.Shop.Model.Product;
import com.Shop.Repository.OrderItemRepository;
import com.Shop.Repository.OrderRepository;
import com.Shop.Repository.ProductRepository;
import com.Shop.Service.Generic.GenericService;
import com.Shop.Service.OrderService.OrderService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductService extends GenericService<Product> implements IProductSV {
	@Autowired
	public ProductService(JpaRepository<Product, Long> gmRepository) {
		super(gmRepository);

	}

	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderService orderService;

	public Map<String, Object> getProductInformation() {
		Map<String, Object> productInformation = new HashMap<>();

		// Lấy tổng số sản phẩm
		Long totalProductCount = ((ProductRepository) genericRepository).getTotalProductCount();
		productInformation.put("totalProductCount", totalProductCount);

		// Lấy 5 sản phẩm có amount nhỏ nhất
		List<Product> top5ProductsByAmount = ((ProductRepository) genericRepository).findTop5ProductsByAmount();
		productInformation.put("top5ProductsRemaining", top5ProductsByAmount);

		// Lấy 5 sản phẩm có (numberInit - amount) lớn nhất
		List<Product> top5ProductsByNumberInitMinusAmount = ((ProductRepository) genericRepository)
				.findTop5ProductsByNumberInitMinusAmount();
		productInformation.put("top5ProductsSold", top5ProductsByNumberInitMinusAmount);
		LocalDateTime startDate = LocalDateTime.now();
		Long number = orderRepository.countALLSoldProductsByProductAndMonth(startDate.getYear(),
				startDate.getMonthValue());

		productInformation.put("productsSold", number);
		return productInformation;
	}

	public Map<String, Object> getByCategory(Long id, int off, int ind, String sortBy) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(off, ind, Sort.by(sortBy));
		Page<Product> pagedResult = ((ProductRepository) genericRepository).findByCategoryId(id, paging);

		Map<String, Object> response = new HashMap<>();

		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());

		if (pagedResult.hasContent()) {
			response.put("products", pagedResult.getContent());
		} else {
			response.put("products", new ArrayList<>());
		}
		return response;

	}

	public Map<String, Object> findProductsByPage(int off, int ind, String sortBy) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(off, ind, Sort.by(sortBy));
		Page<Product> pagedResult = ((ProductRepository) genericRepository).findAll(paging);

		Map<String, Object> response = new HashMap<>();
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());

		if (pagedResult.hasContent()) {
			response.put("products", pagedResult.getContent());
		} else {
			response.put("products", new ArrayList<>());
		}

		return response;

	}

	public List<Product> findProductsByName(String name) {

		List<Product> result = ((ProductRepository) genericRepository).findByNameContainingIgnoreCase(name);

		return result;
	}

	@Transactional
	public void deleteProduct(Long productId) {
		Product product = ((ProductRepository) genericRepository).findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		// Xóa sản phẩm khỏi các đơn hàng (OrderItem) có liên quan trước khi xóa sản
		// phẩm
		List<OrderItem> orderItems = orderItemRepository.findByProduct(product);
		orderItems.forEach(orderItem -> {
			Order order = orderRepository.findByOrderItemsId(orderItem.getId()).get(0);
			List<OrderItem> listItem = order.getOrderItems();
			listItem.remove(orderItem);
			order.setOrderItems(listItem);
			orderRepository.save(order);
			orderItemRepository.delete(orderItem);
		});
//		orderItemRepository.saveAll(orderItems);

		// Xóa sản phẩm
		((ProductRepository) genericRepository).delete(product);
	}

	public List<CateDTO> findCateInfo() {

		List<CateDTO> result = ((ProductRepository) genericRepository).getCategoriesWithCount();

		return result;
	}

	@Override
	public List<Product> getByCategory(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
