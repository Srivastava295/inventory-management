package com.ecommerce.inventorymanagement.constants;

public class StatusConstants {
	public static final String SUCCESS_MESSAGE = "Success";
	public static final String REQUIRED_FIELDS_MISSING = "Required fields are missing";
	public static final String INSUFFICIENT_PERMISSIONS = "Insufficient permission";
	public static final String NO_CONTENT_FOUND = "No content found";
	public static final String DATABASE_ERROR = "Error updating the database";
	public static final String DATABASE_SUCCESS = "Database successfully updated";
	public static final String AUTH_TOKEN_EXPIRED = "Authorization token expired";
	public static final String AUTH_TOKEN_MISSING = "Authorization token missing";
	public static final String REG_FAILED = "Error while registering the user";
	public static final String ALREADY_REG = "Email already exists";
	public static final String INCORRECT_PASS = "Incorrect password";
	public static final String INCORRECT_EMAIL = "Invalid email";
	public static final String TAMPERED_JWT = "JWT token is tampered";

	public static final int OK = 200;
	public static final int NO_CONTENT = 204;
	public static final int AUTH_FAILED = 401;
	public static final int FORBIDDEN = 403;
	public static final int DUPLICATE = 409;
	public static final int UNPROCESSABLE = 422;
	public static final int SERVER_ERROR = 500;
}