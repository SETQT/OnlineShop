package com.Shop.DTO;

public class ClientDTO {

	private Long id;
	private String name;
	private String address;
	private String phoneNumber;
	private String email;
	private Double moneyUsed;
	private String moneyString;

	public Long getId() {
		return id;
	}

	public String getMoneyString() {
		return moneyString;
	}

	public void setMoneyString(String moneyString) {
		this.moneyString = moneyString;
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

	public double getMoneyUsed() {
		return moneyUsed;
	}

	public void setMoneyUsed(Double moneyUsed) {
		this.moneyUsed = moneyUsed;
	}

	public ClientDTO(Long id, String name, String address, String phoneNumber, String email, double moneyUsed) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.moneyUsed = moneyUsed;
	}

	public ClientDTO() {

	}
}
