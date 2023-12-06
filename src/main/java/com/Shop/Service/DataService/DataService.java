package com.Shop.Service.DataService;

import java.util.List;

import com.Shop.Model.Category;
import com.Shop.Model.Client;
import com.Shop.Model.OrderItem;
import com.Shop.Model.Product;

public interface DataService {
	List<Category> getAllCategories();

	List<Client> getAllClients();

	List<OrderItem> getAllOrderItems();

	List<Product> getAllProducts();
	// Các phương thức khác tùy thuộc vào nhu cầu của bạn
}
