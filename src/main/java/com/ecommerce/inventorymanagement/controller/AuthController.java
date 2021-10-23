package com.ecommerce.inventorymanagement.controller;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.inventorymanagement.constants.Constants;
import com.ecommerce.inventorymanagement.constants.StatusConstants;
import com.ecommerce.inventorymanagement.dto.UserDTO;
import com.ecommerce.inventorymanagement.service.AuthService;
import com.ecommerce.inventorymanagement.util.CommonUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	private static final Logger LOG = Logger.getLogger(AuthController.class.getName());

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDto, BindingResult result) {
		LOG.log(Level.INFO, "User registration initiated");
		JSONObject responseJson = new JSONObject();
		// Check if request body mapped correctly to UserDTO
		if (result.hasErrors()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.UNPROCESSABLE, Constants.FAILURE,
					StatusConstants.REQUIRED_FIELDS_MISSING, null);

		} else {
			responseJson = authService.register(userDto);
		}

		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestBody Map<String, String> params) {
		LOG.log(Level.INFO, "User login initiated");
		JSONObject responseJson = new JSONObject();
		String email = params.get("email");
		String password = params.get("password");
		// Check if required parameters are passed
		if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
			responseJson = authService.login(email, password);
		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.UNPROCESSABLE, Constants.FAILURE,
					StatusConstants.REQUIRED_FIELDS_MISSING, null);
		}

		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/addToGroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addToGroup(@RequestBody Map<String, String> params, @RequestAttribute String email) {
		LOG.log(Level.INFO, "User addition to groups initiated");
		JSONObject responseJson = new JSONObject();
		String grpId = params.get("grpId");
		String userId = params.get("userId");
		// Check if required parameters are passed
		if (grpId != null && !grpId.isEmpty() && userId != null && !userId.isEmpty() && email != null
				&& !email.isEmpty()) {
			// Check if the currently logged in user is an Admin
			if (authService.isAdmin(email)) {
				responseJson = authService.addUserToGroup(Long.parseLong(params.get("grpId")),
						Long.parseLong(params.get("userId")));
			} else {
				responseJson = CommonUtil.getResponseJson(StatusConstants.FORBIDDEN, Constants.FAILURE,
						StatusConstants.INSUFFICIENT_PERMISSIONS, null);
			}

		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.UNPROCESSABLE, Constants.FAILURE,
					StatusConstants.REQUIRED_FIELDS_MISSING, null);
		}

		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);

	}

	@RequestMapping(value = "/getUserGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getUserGroups() {
		JSONObject responseJson = new JSONObject();
		JSONObject allGroups = authService.getAllGroups();
		// Check if any group data is retrieved from DB
		if (allGroups.isEmpty()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.NO_CONTENT, Constants.FAILURE,
					StatusConstants.NO_CONTENT_FOUND, null);

		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
					StatusConstants.SUCCESS_MESSAGE, allGroups);
		}

		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getUsers() {
		
		JSONObject responseJson = new JSONObject();
		JSONObject allUsers = authService.getAllUsers();
		// Check if any group data is retrieved from DB
		if (allUsers.isEmpty()) {
			responseJson = CommonUtil.getResponseJson(StatusConstants.NO_CONTENT, Constants.FAILURE,
					StatusConstants.NO_CONTENT_FOUND, null);

		} else {
			responseJson = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
					StatusConstants.SUCCESS_MESSAGE, allUsers);
		}

		return new ResponseEntity<String>(responseJson.toString(), HttpStatus.OK);
	}
}
