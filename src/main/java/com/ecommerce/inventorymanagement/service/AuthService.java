package com.ecommerce.inventorymanagement.service;

import org.json.JSONObject;

import com.ecommerce.inventorymanagement.dto.UserDTO;
import com.ecommerce.inventorymanagement.model.UserGroup;

public interface AuthService {

	JSONObject register(UserDTO userDto);

	JSONObject login(String email, String password);

	JSONObject addUserToGroup(Long grpId, Long userId);

	boolean isAdmin(String email);

	boolean hasPermission(Long groupId, String email);

	JSONObject getAllGroups();

	UserGroup getDefaultGroup();

	JSONObject getAllUsers();

}
