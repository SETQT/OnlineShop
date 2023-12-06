package com.Shop.Service.CategotyService;

import org.springframework.stereotype.Service;

import com.Shop.Model.Category;
import com.Shop.Repository.CategoryRepository;
import com.Shop.Service.Generic.GenericService;

@Service
public class CategoryService extends GenericService<Category> implements ICategorySV {
	public CategoryService(CategoryRepository gmRepository) {
		super(gmRepository);
	}

}
