package com.example.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthoritiesLoggingAfterFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(AuthoritiesLoggingAfterFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("AuthoritiesLoggingAfterFilter:: doFilter");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			logger.info("User " + authentication.getName() + " is successfully authenticated and " + "has the authorities "
					+ authentication.getAuthorities().toString());
		}
		chain.doFilter(request, response);
	}

}