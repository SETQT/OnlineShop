package com.Shop.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "clients")
@SuperBuilder
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private String phoneNumber;
	private String email;

	public Client() {
	}

	public Client(Long id, String name, String address, String phoneNumber, String email) {
		this.id = id;
//		this.user = u;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public Long getId() {
		return id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return "Client{" + "id=" + id + ", name='" + name + '\'' + ", address='" + address + '\'' + ", phoneNumber='"
				+ phoneNumber + '\'' + ", email='" + email + '\'' + '}';
	}
}
