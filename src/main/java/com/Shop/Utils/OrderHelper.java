package com.Shop.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.Shop.DTO.OrderDTO;
import com.Shop.Model.OrderItem;
import com.Shop.Model.Product;
import com.Shop.Service.ProductService.ProductService;

public class OrderHelper {

	public OrderHelper(ProductService productService) {
		this.productService = productService;
	}

	ProductService productService;

	public List<OrderItem> convertDTOToOrderItemList(OrderDTO orderDTO) {
		List<Long> productIds = orderDTO.getProductIds();
		List<Integer> quantities = orderDTO.getQuantities();
		List<Double> listPrice = orderDTO.getPrice();

		if (productIds.size() != quantities.size()) {
			throw new IllegalArgumentException("Số lượng sản phẩm không khớp với số lượng");
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (int i = 0; i < productIds.size(); i++) {
			Long productId = productIds.get(i);
			Integer quantity = quantities.get(i);
			Double price = listPrice.get(i);
			Optional<Product> product;
			try {
				product = productService.findById(productId);
				if (product.isPresent()) {
					OrderItem orderItem = new OrderItem();
					orderItem.setProduct(product.get());
					orderItem.setQuantity(quantity);
					orderItem.setProfit(product.get().getProfit());
					orderItems.add(orderItem);
					if (price != null)
						orderItem.setPrice(price);
					else
						orderItem.setPrice(product.get().getPrice());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return orderItems;
	}
}
