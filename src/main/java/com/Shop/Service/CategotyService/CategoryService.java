package com.Shop.Service.CategotyService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shop.Model.Category;
import com.Shop.Model.Product;
import com.Shop.Repository.CategoryRepository;
import com.Shop.Repository.ProductRepository;
import com.Shop.Service.Generic.GenericService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService extends GenericService<Category> implements ICategorySV {
	public CategoryService(CategoryRepository gmRepository) {
		super(gmRepository);
	}

	@Autowired
	private ProductRepository productRepository;

	@Transactional
	public void deleteCategory(Long categoryId) {
		Category category = ((CategoryRepository) genericRepository).findById(categoryId)
				.orElseThrow(() -> new EntityNotFoundException("Category not found"));

		// Lấy danh sách sản phẩm thuộc Category
		List<Product> productsInCategory = productRepository.findByCategory(category);

		// Đặt null cho category của các sản phẩm thuộc Category
		productsInCategory.forEach(product -> product.setCategory(null));
		productRepository.saveAll(productsInCategory);

		// Xóa Category
		((CategoryRepository) genericRepository).delete(category);
	}
}
