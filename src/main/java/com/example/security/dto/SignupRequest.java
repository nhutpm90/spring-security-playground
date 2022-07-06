package com.example.security.dto;

import java.util.List;

public class SignupRequest extends LoginRequest {

	private List<String> roles;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
