package com.ecommerce.inventorymanagement;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryManagementApplication {

	private static final Logger LOG = Logger.getLogger(InventoryManagementApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
		LOG.log(Level.INFO, "Inventory Management application is started");
	}

}
