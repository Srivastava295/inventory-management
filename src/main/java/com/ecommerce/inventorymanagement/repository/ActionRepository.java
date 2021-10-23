package com.ecommerce.inventorymanagement.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecommerce.inventorymanagement.model.Action;

public interface ActionRepository extends CrudRepository<Action, Long> {

}
