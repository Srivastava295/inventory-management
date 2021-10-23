package com.ecommerce.inventorymanagement.service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ecommerce.inventorymanagement.dto.ProductDTO;
import com.ecommerce.inventorymanagement.model.Category;

public interface ProductService {

	boolean save(ProductDTO productDto);

	boolean update(ProductDTO productDto);

	boolean delete(long id);

	JSONObject getById(long id);

	JSONObject getAllProducts();

	JSONArray getByCategoryId(long parentId, JSONArray products);

	JSONObject getAllCategories();

	Category getDefaultCategory();
}