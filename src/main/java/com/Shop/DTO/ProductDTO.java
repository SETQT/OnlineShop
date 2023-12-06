package com.Shop.DTO;

public class ProductDTO {

	private Long id;

	private String name;
	private double price;

	private Long category;
	private String image;

	private Long amount;

	private double discount;
	private double profit;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

}
