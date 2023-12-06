package com.Shop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Shop.Model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	@Query("SELECT c FROM Client c WHERE c.phoneNumber = :number")
	public Client findByPhoneNumber(String number);
}
