package com.Shop.Model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "UserOrder")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime orderDate;

	@ManyToOne
	private Client client;

	private double total;
	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;

	// Các phương thức getter và setter

	public void setTotal(double total) {
		this.total = total;
	}

	public Long getId() {
		return id;
	}

	public Order() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public double getTotal() {
		return total;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public void calculateTotal() {
		double Stotal = 0.0;
		if (orderItems != null) {
			for (OrderItem item : orderItems) {
				Stotal += item.getQuantity() * item.getPrice();
			}
		}
		this.setTotal(Stotal);
	}
}
