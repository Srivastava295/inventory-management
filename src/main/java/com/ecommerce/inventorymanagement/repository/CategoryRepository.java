package com.ecommerce.inventorymanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ecommerce.inventorymanagement.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	List<Category> findByName(String name);
	List<Category> findByParentId(Category parentId);
}
