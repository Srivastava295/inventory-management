package com.ecommerce.inventorymanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ecommerce.inventorymanagement.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByEmail(String email);
}
