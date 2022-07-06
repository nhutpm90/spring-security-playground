package com.example.security.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.entity.Customer;
import com.example.security.repo.CustomerRepo;

@RestController
public class UserController {

	@Autowired
	private CustomerRepo customerRepo;

	@GetMapping("/user/me")
	@PreAuthorize("isFullyAuthenticated()")
	public Customer me() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return this.customerRepo.findByEmail(auth.getName()).get(0);
	}
}

