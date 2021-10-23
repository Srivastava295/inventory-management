package com.ecommerce.inventorymanagement.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ecommerce.inventorymanagement.model.UserGroup;

public interface GroupRepository extends CrudRepository<UserGroup, Long> {
	List<UserGroup> findByName(String name);
}
