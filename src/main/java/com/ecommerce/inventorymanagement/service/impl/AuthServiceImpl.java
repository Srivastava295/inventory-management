package com.ecommerce.inventorymanagement.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.inventorymanagement.constants.Constants;
import com.ecommerce.inventorymanagement.constants.StatusConstants;
import com.ecommerce.inventorymanagement.dto.UserDTO;
import com.ecommerce.inventorymanagement.model.User;
import com.ecommerce.inventorymanagement.model.UserGroup;
import com.ecommerce.inventorymanagement.repository.GroupRepository;
import com.ecommerce.inventorymanagement.repository.UserRepository;
import com.ecommerce.inventorymanagement.service.AuthService;
import com.ecommerce.inventorymanagement.util.CommonUtil;
import com.ecommerce.inventorymanagement.util.ConfigMgrUtil;
import com.ecommerce.inventorymanagement.util.EncryptionUtil;
import com.ecommerce.inventorymanagement.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	EncryptionUtil encryptionUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	ConfigMgrUtil configMgr;

	private static final Logger LOG = Logger.getLogger(AuthServiceImpl.class.getName());

	@Override
	public JSONObject register(UserDTO userDto) {
		JSONObject response = new JSONObject();
		LOG.log(Level.INFO, "DB interaction: finding users by email");
		List<User> userList = userRepository.findByEmail(userDto.getEmail());
		// checking if user with same mail id exists
		if (userList.isEmpty()) {
			// Creating new user
			User user = new User();
			user.setUsername(userDto.getUserName());
			user.setEmail(userDto.getEmail());
			user.setPassword(encryptionUtil.encrypt(userDto.getPassword()));
			LOG.log(Level.INFO, "DB interaction: adding new user");
			User savedUser = userRepository.save(user);
			if (savedUser != null) {
				response = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
						StatusConstants.SUCCESS_MESSAGE, null);
			} else {
				response = CommonUtil.getResponseJson(StatusConstants.SERVER_ERROR, Constants.FAILURE,
						StatusConstants.REG_FAILED, null);
			}
		} else {
			response = CommonUtil.getResponseJson(StatusConstants.DUPLICATE, Constants.FAILURE,
					StatusConstants.ALREADY_REG, null);
		}
		return response;

	}

	@Override
	public JSONObject login(String email, String password) {
		JSONObject response = new JSONObject();
		LOG.log(Level.INFO, "DB interaction: finding users by email");
		List<User> userList = userRepository.findByEmail(email);
		// checking if the user with given mail id exists
		if (!userList.isEmpty()) {
			User dbUser = userList.get(0);
			// matching the password
			if (dbUser.getPassword().equals(encryptionUtil.encrypt(password))) {
				Map<String, Object> data = new HashMap<>();
				data.put("email", email);
				LOG.log(Level.INFO, "Generating JWT");
				String jwt = JwtUtil.generateToken(data, configMgr.getPropertyValueAsString(Constants.JWT_SECRET_KEY));
				response = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
						StatusConstants.SUCCESS_MESSAGE, jwt);
			} else {
				// incorrect password
				response = CommonUtil.getResponseJson(StatusConstants.AUTH_FAILED, Constants.FAILURE,
						StatusConstants.INCORRECT_PASS, null);
			}
		} else {
			// no user with given mail id
			response = CommonUtil.getResponseJson(StatusConstants.AUTH_FAILED, Constants.FAILURE,
					StatusConstants.INCORRECT_EMAIL, null);
		}
		return response;

	}

	@Override
	public JSONObject addUserToGroup(Long grpId, Long userId) {
		JSONObject response = new JSONObject();
		LOG.log(Level.INFO, "DB interaction: finding users by email");
		Optional<User> dbUser = userRepository.findById(userId);
		LOG.log(Level.INFO, "DB interaction: finding group by groupId");
		Optional<UserGroup> dbGroup = groupRepository.findById(grpId);
		// checking if the given user and given group exists
		if (dbUser.isPresent() && dbGroup.isPresent()) {
			User user = dbUser.get();
			user.addGroup(dbGroup.get());
			LOG.log(Level.INFO, "DB interaction: adding user to group");
			userRepository.save(user);
			response = CommonUtil.getResponseJson(StatusConstants.OK, Constants.SUCCESS,
					StatusConstants.SUCCESS_MESSAGE, null);
		} else {
			response = CommonUtil.getResponseJson(StatusConstants.UNPROCESSABLE, Constants.FAILURE,
					StatusConstants.DATABASE_ERROR, null);
		}
		return response;
	}

	@Override
	public boolean isAdmin(String email) {
		LOG.log(Level.INFO, "DB interaction: finding users by email");
		User dbUser = userRepository.findByEmail(email).get(0);
		// admin group id from the configuration
		long adminGroupId = configMgr.getPropertyValueAsLong(Constants.ADMIN_GROUP);
		Set<UserGroup> groups = dbUser.getGroups();
		for (UserGroup group : groups) {
			System.out.println("groups:" + group.getName());
			// checking if the user belongs to admin group
			if (group.getId() == adminGroupId)
				return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(Long groupId, String email) {
		// checking if the user belongs to admin group
		if (isAdmin(email)) {
			return true;
		} else {
			LOG.log(Level.INFO, "DB interaction: finding users by email");
			User user = userRepository.findByEmail(email).get(0);
			Set<UserGroup> groups = user.getGroups();
			for (UserGroup group : groups) {
				// checking if the user belongs to the given groupId
				if (group.getId() == groupId)
					return true;
			}
		}
		return false;
	}

	@Override
	public JSONObject getAllGroups() {
		LOG.log(Level.INFO, "DB interaction: finding all groups");
		Iterable<UserGroup> dbGroups = groupRepository.findAll();
		JSONArray groups = new JSONArray();
		for (UserGroup userGroup : dbGroups) {
			// creating JSON object for each group
			groups.put(getGroupJson(userGroup));
		}
		JSONObject response = new JSONObject();
		response.put("groups", groups);
		return response;
	}

	@Override
	public JSONObject getAllUsers() {
		LOG.log(Level.INFO, "DB interaction: finding all users");
		Iterable<User> dbUsers = userRepository.findAll();
		JSONArray groups = new JSONArray();
		for (User user : dbUsers) {
			// creating JSON object for each group
			groups.put(getUserJson(user));
		}
		JSONObject response = new JSONObject();
		response.put("users", groups);
		return response;
	}
	
	@Override
	public UserGroup getDefaultGroup() {
		// getting default group from configuration
		long defaultGroup = configMgr.getPropertyValueAsLong(Constants.DEFAULT_GROUP);
		return groupRepository.findById(defaultGroup).get();
	}

	private JSONObject getGroupJson(UserGroup userGroup) {
		JSONObject groupObj = new JSONObject();
		groupObj.put("id", userGroup.getId());
		groupObj.put("name", userGroup.getName());

		return groupObj;
	}
	
	private JSONObject getUserJson(User user) {
		JSONObject groupObj = new JSONObject();
		groupObj.put("id", user.getId());
		groupObj.put("username", user.getUsername());
		groupObj.put("email", user.getEmail());
		return groupObj;
	}
}
