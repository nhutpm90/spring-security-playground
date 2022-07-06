package com.example.security.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.dto.JwtWrapper;
import com.example.security.dto.LoginRequest;
import com.example.security.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class AuthController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/auth/login")
	public JwtWrapper login(@RequestBody LoginRequest loginRequest) throws JsonProcessingException {
		return this.customerService.login(loginRequest.getEmail(), loginRequest.getPassword());
	}
}
