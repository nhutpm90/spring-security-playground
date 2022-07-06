package com.example.security.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.security.entity.Authority;
import com.example.security.entity.Customer;
import com.example.security.repo.CustomerRepo;
import com.example.security.service.CustomerService;
import com.example.security.utils.JwtUtils;

@Component
public class DataInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public DataInitializer() {
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("-----------DataInitializer-----------");
		
		Customer user = new Customer("user@gmail.com", passwordEncoder.encode("123456"));
		user.addAuthority(new Authority("ROLE_USER"));
		this.customerService.saveIfNotExist(user);
		
		Customer admin = new Customer("admin@gmail.com", passwordEncoder.encode("123456"));
		admin.addAuthority(new Authority("ROLE_ADMIN"));
		this.customerService.saveIfNotExist(admin);
	}
}
