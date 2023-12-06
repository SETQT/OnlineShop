package com.Shop.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.experimental.SuperBuilder;

@Entity

@SuperBuilder
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String image;
	private double price;

	@ManyToOne
	private Category category;

	private Long amount;
	private double discount;
	private double profit;

	public Product() {
	}

	public Product(String name, double price, Long amount, double discount, String image, double profit) {
		super();
		this.image = image;
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.discount = discount;
		this.profit = profit;
	}

	public Long getAmount() {
		return amount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}
}
