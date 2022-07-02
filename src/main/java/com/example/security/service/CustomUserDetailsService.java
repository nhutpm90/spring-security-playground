package com.example.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security.entity.Customer;
import com.example.security.entity.CustomerUserDetails;
import com.example.security.repo.CustomerRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepo customerRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Customer> customer = customerRepo.findByEmail(username);
		if (customer.size() == 0) {
			throw new UsernameNotFoundException("User details not found for the user : " + username);
		}
		return new CustomerUserDetails(customer.get(0));
	}

}
