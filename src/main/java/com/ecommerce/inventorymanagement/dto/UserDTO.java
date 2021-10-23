package com.ecommerce.inventorymanagement.dto;

import javax.validation.constraints.NotEmpty;

//Data transfer object for transferring User data between application layers
public class UserDTO {

	@NotEmpty
	private String userName;
	@NotEmpty
	private String password;
	@NotEmpty
	private String email;

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
