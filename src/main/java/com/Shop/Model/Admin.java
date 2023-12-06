package com.Shop.Model;

import com.Shop.Security.Model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String shopName;
	private String shopAddress;
	private String phoneNumber;
	private String email;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	@JsonBackReference(value = "user_employer")
	private User user;

	public Admin() {
	}

	public Admin(Long id, String username, String password, String fullName, String shopAddress, String phoneNumber,
			String email) {
		this.id = id;
		this.shopName = fullName;
		this.shopAddress = shopAddress;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Admin{" + "id=" + id + ", fullName='" + shopName + '\'' + ", shopAddress='" + shopAddress + '\''
				+ ", phoneNumber='" + phoneNumber + '\'' + ", email='" + email + '\'' + '}';
	}
}
