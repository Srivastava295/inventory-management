package com.ecommerce.inventorymanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ecommerce.inventorymanagement.model.Category;
import com.ecommerce.inventorymanagement.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	List<Product> findByCategory(Category category);
}
