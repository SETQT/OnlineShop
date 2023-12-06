package com.Shop.Service.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.Shop.Model.Client;
import com.Shop.Repository.ClientRepository;
import com.Shop.Service.Generic.GenericService;

@Service
public class ClientService extends GenericService<Client> implements IClientSV {

	@Autowired
	public ClientService(JpaRepository<Client, Long> gmRepository) {
		super(gmRepository);
	}

	public Client findByPhoneNumber(String phone) {
		Client res = ((ClientRepository) genericRepository).findByPhoneNumber(phone);
		return res;

	}
}
