package com.example.security.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.dto.SignupRequest;
import com.example.security.entity.Customer;
import com.example.security.repo.CustomerRepo;
import com.example.security.service.CustomerService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/me")
	@PreAuthorize("isFullyAuthenticated()")
	public Customer me() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return this.customerRepo.findByEmail(auth.getName());
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Customer> users() {
		return this.customerRepo.findAll();
	}
	
	@PostMapping("/assign-roles")
	@PreAuthorize("hasRole('ADMIN')")
	public Customer assignRoles(@RequestBody SignupRequest request) {
		return this.customerService.assignRoles(request.getEmail(), request.getRoles());
	}
}

