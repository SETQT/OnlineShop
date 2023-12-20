package com.Shop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Shop.DTO.ClientDTO;
import com.Shop.DTO.ResponseObject;
import com.Shop.Service.Client.ClientService;
import com.Shop.Service.OrderService.OrderService;

@RestController
@RequestMapping("/client")
public class ClientCotroller {
	@Autowired
	ClientService clientSV;
	@Autowired
	OrderService orderSV;

	@GetMapping()
	public ResponseEntity<ResponseObject> getAll() {
		List<ClientDTO> clients = null;
		try {
			clients = orderSV.getClientTotalAmountUsed();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Danh sách Client", clients));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "không có Client", null));
		}
	}
}
