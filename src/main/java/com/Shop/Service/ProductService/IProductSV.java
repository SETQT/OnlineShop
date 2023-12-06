package com.Shop.Service.ProductService;

import java.util.List;

import com.Shop.Model.Product;
import com.Shop.Service.Generic.IGenericService;

public interface IProductSV extends IGenericService<Product> {
	public List<Product> getByCategory(Long id);
}
