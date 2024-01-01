package com.Shop.Service.Client;

import java.util.Optional;

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

	public Client createClient(Client client) {
		Optional<Client> c = ((ClientRepository) genericRepository).findById(client.getId());
		if (c.isEmpty())
			return ((ClientRepository) genericRepository).save(client);
		else {
			Client res = c.get();
			res.setAddress(client.getAddress());
			res.setEmail(client.getEmail());
			res.setName(client.getName());
			res.setId(client.getId());
			res.setPhoneNumber(client.getPhoneNumber());
			((ClientRepository) genericRepository).save(res);
			return res;
		}
	}
//	public void delete(String id) {
//
//		Client c= ((ClientRepository) genericRepository).getById(Long.parseLong(id));
//		
//	}
}
