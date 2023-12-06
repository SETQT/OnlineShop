package com.Shop.Service.ProductService;

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

import com.Shop.Model.Product;
import com.Shop.Repository.ProductRepository;
import com.Shop.Service.Generic.GenericService;

@Service
public class ProductService extends GenericService<Product> implements IProductSV {
	@Autowired
	public ProductService(JpaRepository<Product, Long> gmRepository) {
		super(gmRepository);

	}

	public Map<String, Object> getByCategory(Long id, int off, int ind, String sortBy) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(off, ind, Sort.by(sortBy));
		Page<Product> pagedResult = ((ProductRepository) genericRepository).findByCategoryId(id, paging);

		Map<String, Object> response = new HashMap<>();
		response.put("orders", pagedResult.getContent());
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

	public List<Product> findProductsByPage(int off, int ind, String sortBy) {
		// TODO Auto-generated method stub
		Pageable paging = PageRequest.of(off, ind, Sort.by(sortBy));
		Page<Product> pagedResult = ((ProductRepository) genericRepository).findAll(paging);

		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Product>();
		}

	}

	public List<Product> findProductsByName(String name) {

		List<Product> result = ((ProductRepository) genericRepository).findByNameContainingIgnoreCase(name);

		return result;
	}

	@Override
	public List<Product> getByCategory(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
