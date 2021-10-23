package com.ecommerce.inventorymanagement.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.inventorymanagement.constants.Constants;
import com.ecommerce.inventorymanagement.constants.StatusConstants;
import com.ecommerce.inventorymanagement.dto.ProductDTO;
import com.ecommerce.inventorymanagement.service.AuthService;
import com.ecommerce.inventorymanagement.service.ProductService;
import com.ecommerce.inventorymanagement.util.CommonUtil;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	ProductService productService;

	@Autowired
	AuthService authService;

	private static final Logger LOG = Logger.getLogger(ProductController.class.getName());

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> add(@RequestBody @Valid ProductDTO productDto, BindingResult result,
			@RequestAttribute String email) {
		LOG.log(Level.INFO, "Add product initiated");
		JSONObject responseJson = new JSONObject();
		// Check if request body is mapped correctly to ProductDTO
		if (result.hasErrors()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.UNPROCESSABLE, Constants.FAILURE,
					StatusConstants.REQUIRED_FIELDS_MISSING, null);
		} else if (authService.hasPermission(productDto.getGroupId(), email)) {
			// Proceeding if the user has required permissions
			if (productService.save(productDto)) {
				responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
						StatusConstants.DATABASE_SUCCESS, null);
			} else {
				responseJson = CommonUtil.getResponseJson(StatusConstants.SERVER_ERROR, Constants.FAILURE,
						StatusConstants.DATABASE_ERROR, null);
			}
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.FORBIDDEN, Constants.FAILURE,
					StatusConstants.INSUFFICIENT_PERMISSIONS, null);
		}
		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(@RequestBody @Valid ProductDTO productDto, BindingResult result,
			@RequestAttribute String email) {
		LOG.log(Level.INFO, "Update product initiated");
		JSONObject responseJson = new JSONObject();
		// Check if request body is mapped correctly to ProductDTO
		if (result.hasErrors() || productDto.getId() == null) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.UNPROCESSABLE, Constants.FAILURE,
					StatusConstants.REQUIRED_FIELDS_MISSING, null);
		} else if (authService.hasPermission(productDto.getGroupId(), email)) {
			// Proceeding if the user has required permissions
			if (productService.update(productDto)) {
				responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
						StatusConstants.DATABASE_SUCCESS, null);
			} else {
				responseJson = CommonUtil.getResponseJson(StatusConstants.SERVER_ERROR, Constants.FAILURE,
						StatusConstants.DATABASE_ERROR, null);
			}
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.FORBIDDEN, Constants.FAILURE,
					StatusConstants.INSUFFICIENT_PERMISSIONS, null);
		}
		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(@RequestParam("id") @NotEmpty long id, @RequestAttribute String email) {
		LOG.log(Level.INFO, "Delete product initiated");
		JSONObject responseJson = new JSONObject();
		JSONObject product = productService.getById(id);
		if (authService.hasPermission(product.getLong("groupId"), email)) {
			// Proceeding if the user has required permissions
			if (productService.delete(id)) {
				responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
						StatusConstants.DATABASE_SUCCESS, null);
			} else {
				responseJson = CommonUtil.getResponseJson(StatusConstants.SERVER_ERROR, Constants.FAILURE,
						StatusConstants.DATABASE_ERROR, null);
			}
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.FORBIDDEN, Constants.FAILURE,
					StatusConstants.INSUFFICIENT_PERMISSIONS, null);
		}
		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getById(@RequestParam("id") @NotEmpty long id) {
		LOG.log(Level.INFO, "Get product by Id initiated");
		JSONObject responseJson = new JSONObject();
		JSONObject dbResponse = productService.getById(id);
		if (dbResponse.isEmpty()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.NO_CONTENT, Constants.FAILURE,
					StatusConstants.NO_CONTENT_FOUND, null);
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
					StatusConstants.SUCCESS_MESSAGE, dbResponse);
		}
		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/getByCategory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getByCategory(@RequestParam("categoryId") @NotEmpty long catId) {
		LOG.log(Level.INFO, "Get product by category initiated");
		JSONObject responseJson = new JSONObject();
		JSONArray dbResponse = productService.getByCategoryId(catId, new JSONArray());
		if (dbResponse.isEmpty()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.NO_CONTENT, Constants.FAILURE,
					StatusConstants.NO_CONTENT_FOUND, null);
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
					StatusConstants.SUCCESS_MESSAGE, dbResponse);
		}
		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAllProducts() {
		LOG.log(Level.INFO, "Get all products product initiated");
		JSONObject responseJson = new JSONObject();
		JSONObject dbResponse = productService.getAllProducts();
		if (dbResponse.isEmpty()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.NO_CONTENT, Constants.FAILURE,
					StatusConstants.NO_CONTENT_FOUND, null);
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
					StatusConstants.SUCCESS_MESSAGE, dbResponse);
		}
		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getCategories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getCategories() {
		LOG.log(Level.INFO, "Get product categories initiated");
		JSONObject responseJson = new JSONObject();
		JSONObject allCategories = productService.getAllCategories();
		if (allCategories.isEmpty()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.NO_CONTENT, Constants.FAILURE,
					StatusConstants.NO_CONTENT_FOUND, null);
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
					StatusConstants.SUCCESS_MESSAGE, allCategories);
		}
		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
	}
}
