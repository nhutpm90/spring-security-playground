package com.example.security.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.security.constants.SecurityConstants;
import com.example.security.dto.JwtWrapper;
import com.example.security.entity.Customer;
import com.example.security.filter.JWTTokenValidatorFilter;
import com.example.security.repo.CustomerRepo;
import com.example.security.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerService {

	private final Logger logger = LoggerFactory.getLogger(CustomerService.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private ObjectMapper jsonUtils;
	
	private String issuer = "My App";
	private String subject = "JWT Token Test";
	
	public String JWT_KEY = SecurityConstants.JWT_KEY;
	public Integer JWT_EXPIRATION_IN_MS = SecurityConstants.JWT_EXPIRATION_IN_MS;
	
	public JwtWrapper login(String email, String password) throws JsonProcessingException {
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password));
		Customer customer = customerRepo.findByEmail(authentication.getPrincipal().toString()).get(0);
		
		String username = customer.getEmail();
		String authorities = populateAuthorities(authentication.getAuthorities());
		String jwtToken = JwtUtils.generateTokenFromUsername(issuer, subject, username, authorities, 
				JWT_KEY, JWT_EXPIRATION_IN_MS);
		
		List<String> roles = authentication.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		String refreshToken = "";
		JwtWrapper response = new JwtWrapper(username, jwtToken, refreshToken, roles);
		
		logger.info(String.format("CustomerService:: login:: email:: %s - password:: %s - token:: %s",  email, password, jwtToken));
		return response;
	}
	
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
        	authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
	}
}
