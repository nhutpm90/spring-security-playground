package com.example.security.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.security.entity.Authority;
import com.example.security.entity.Customer;
import com.example.security.repo.CustomerRepo;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String pwd = authentication.getCredentials().toString();
		Customer customer = customerRepo.findByEmail(username);
		if (customer != null) {
			if (passwordEncoder.matches(pwd, customer.getPwd())) {
				return new UsernamePasswordAuthenticationToken(username, pwd, convertToGrantedAuthorities(customer.getAuthorities()));
			} else {
				throw new BadCredentialsException("Invalid password!");
			}
		} else {
			throw new BadCredentialsException("No user registered with this details!");
		}
	}

	private Set<GrantedAuthority> convertToGrantedAuthorities(Set<Authority> authorities) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Authority authority : authorities) {
        	grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }
	
	@Override
	public boolean supports(Class<?> authenticationType) {
		return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
	}
}
