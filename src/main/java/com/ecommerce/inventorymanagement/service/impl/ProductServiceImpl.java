package com.ecommerce.inventorymanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.inventorymanagement.constants.Constants;
import com.ecommerce.inventorymanagement.dto.ProductDTO;
import com.ecommerce.inventorymanagement.model.Category;
import com.ecommerce.inventorymanagement.model.Product;
import com.ecommerce.inventorymanagement.model.UserGroup;
import com.ecommerce.inventorymanagement.repository.CategoryRepository;
import com.ecommerce.inventorymanagement.repository.GroupRepository;
import com.ecommerce.inventorymanagement.repository.ProductRepository;
import com.ecommerce.inventorymanagement.service.AuthService;
import com.ecommerce.inventorymanagement.service.ProductService;
import com.ecommerce.inventorymanagement.util.ConfigMgrUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	AuthService authService;

	@Autowired
	private ConfigMgrUtil configMgr;

	private static final Logger LOG = Logger.getLogger(ProductServiceImpl.class.getName());

	@Override
	public boolean save(ProductDTO productDto) {

		Boolean response = false;
		Optional<Category> category = categoryRepository.findById(productDto.getCategoryId());
		Optional<UserGroup> group = groupRepository.findById(productDto.getGroupId());

		// creating product model
		Product product = new Product();
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		product.setStock(productDto.getStock());
		product.setSize(productDto.getSize());
		product.setColor(productDto.getColor());
		product.setCategory(category.orElse(getDefaultCategory()));
		// check if given group exists else get the default
		product.setGroup(group.orElse(authService.getDefaultGroup()));
		product.setActive(true);
		LOG.log(Level.INFO, "DB interaction: adding new product");
		Product savedProduct = productRepository.save(product);
		if (savedProduct != null)
			response = true;
		return response;

	}

	@Override
	public boolean update(ProductDTO productDto) {
		Boolean response = false;
		LOG.log(Level.INFO, "DB interaction: finding product by id");
		Optional<Product> productDB = productRepository.findById(productDto.getId());
		// checking if given product exits in DB
		if (!productDB.isPresent()) {
			return response;
		} else {
			Product product = productDB.get();
			Optional<Category> category = categoryRepository.findById(productDto.getCategoryId());
			Optional<UserGroup> group = groupRepository.findById(productDto.getGroupId());

			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setStock(productDto.getStock());
			product.setSize(productDto.getSize());
			product.setColor(productDto.getColor());
			product.setCategory(category.orElse(getDefaultCategory()));
			product.setGroup(group.orElse(authService.getDefaultGroup()));
			LOG.log(Level.INFO, "DB interaction: updating the product");
			Product savedProduct = productRepository.save(product);
			if (savedProduct != null)
				response = true;
			return response;
		}

	}

	@Override
	public boolean delete(long id) {
		Boolean response = false;
		LOG.log(Level.INFO, "DB interaction: finding product by id");
		Optional<Product> productDB = productRepository.findById(id);
		if (!productDB.isPresent()) {
			return response;
		} else {
			Product product = productDB.get();
			// soft deleting the data by setting a flag
			product.setActive(false);
			LOG.log(Level.INFO, "DB interaction: deleting the products");
			Product savedProduct = productRepository.save(product);
			if (savedProduct != null)
				response = true;
			return response;
		}

	}

	@Override
	public JSONObject getById(long id) {
		JSONObject response = new JSONObject();
		LOG.log(Level.INFO, "DB interaction: finding product by id");
		Optional<Product> productDB = productRepository.findById(id);
		if (productDB.isPresent()) {
			Product product = productDB.get();
			response = getProductJson(product);
		}
		return response;
	}

	@Override
	public JSONObject getAllProducts() {
		LOG.log(Level.INFO, "DB interaction: finding all products");
		Iterable<Product> dbProducts = productRepository.findAll();
		JSONArray products = new JSONArray();
		for (Product product : dbProducts) {
			products.put(getProductJson(product));
		}
		JSONObject response = new JSONObject();
		response.put("products", products);
		return response;
	}

	@Override
	public JSONArray getByCategoryId(long parentId, JSONArray products) {
		LOG.log(Level.INFO, "DB interaction: finding categories with given parent id");
		Category category = categoryRepository.findById(parentId).get();
		products = getCurrentCategoryProducts(category, products);
		List<Category> childCategories = categoryRepository.findByParentId(category);
		for (Category childCategory : childCategories) {
			// making a recursive call to iterate over the children till last
			// depth level
			products = getByCategoryId(childCategory.getId(), products);
		}
		return products;

	}

	@Override
	public JSONObject getAllCategories() {
		LOG.log(Level.INFO, "DB interaction: finding all categories");
		Iterable<Category> dbCategories = categoryRepository.findAll();
		JSONArray categories = new JSONArray();
		for (Category category : dbCategories) {
			categories.put(getCategoryJson(category));
		}
		JSONObject response = new JSONObject();
		response.put("categories", categories);
		return response;
	}

	@Override
	public Category getDefaultCategory() {
		// getting default category from configuration
		long defaultCategory = configMgr.getPropertyValueAsLong(Constants.DEFAULT_CATEGORY);
		return categoryRepository.findById(defaultCategory).get();
	}

	public JSONObject getCategoryJson(Category category) {
		JSONObject categoryObj = new JSONObject();
		categoryObj.put("id", category.getId());
		categoryObj.put("name", category.getName());
		Long parentId = null;
		if (category.getParentId() != null)
			parentId = category.getParentId().getId();
		categoryObj.put("parentId", parentId);

		return categoryObj;
	}

	public JSONArray getCurrentCategoryProducts(Category category, JSONArray products) {
		List<Product> productsDB = productRepository.findByCategory(category);
		for (Product product : productsDB) {
			products.put(getProductJson(product));
		}
		return products;

	}

	// building Product JSON Object
	private JSONObject getProductJson(Product product) {
		JSONObject productJson = new JSONObject();
		productJson.put("id", product.getId());
		productJson.put("name", product.getName());
		productJson.put("description", product.getDescription());
		productJson.put("stock", product.getStock());
		productJson.put("size", product.getSize());
		productJson.put("color", product.getColor());
		productJson.put("categoryId", product.getCategory().getId());
		productJson.put("groupId", product.getGroup().getId());
		return productJson;
	}

}
