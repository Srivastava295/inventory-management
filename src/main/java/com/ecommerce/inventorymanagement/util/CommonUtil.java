package com.ecommerce.inventorymanagement.util;

import org.json.JSONObject;

import com.ecommerce.inventorymanagement.constants.Constants;

public class CommonUtil {

	// creating response object with optional data value
	public static JSONObject getResponseJson(int statusCode, String status, String message, Object data) {
		JSONObject response = new JSONObject();
		response.put(Constants.STATUS_CODE, statusCode);
		response.put(Constants.STATUS, status);
		response.put(Constants.MESSAGE, message);
		if (data != null) {
			response.put(Constants.DATA, data);
		}
		return response;
	}
}
