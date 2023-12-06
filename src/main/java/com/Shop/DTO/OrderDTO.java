package com.Shop.DTO;

import java.util.List;

public class OrderDTO {

	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	private List<Long> productIds;
	private List<Integer> quantities;
	private List<Double> price;

	public List<Long> getProductIds() {
		return productIds;
	}

	public List<Double> getPrice() {
		return price;
	}

	public void setPrice(List<Double> price) {
		this.price = price;
	}

	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}

	public List<Integer> getQuantities() {
		return quantities;
	}

	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}

}
