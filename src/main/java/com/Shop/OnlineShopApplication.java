package com.Shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = { "com.Shop.Security.Model" })
public class OnlineShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

}
