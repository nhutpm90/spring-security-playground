package com.example.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.security.constants.SecurityConstants;
import com.example.security.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
	
	private final Logger logger = LoggerFactory.getLogger(JWTTokenValidatorFilter.class);
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwt = this.parseJwt(request);
		logger.info("--------------JWTTokenValidatorFilter--------------doFilterInternal:: token:: " + jwt);
		if (null != jwt) {
			try {
				Claims claims = JwtUtils.claimJwt(jwt, SecurityConstants.JWT_KEY);
				String username = String.valueOf(claims.get("username"));
				String authorities = (String) claims.get("authorities");
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (ExpiredJwtException ex) {
				logger.error("--------------JWTTokenValidatorFilter--------------expired token");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized: Token Expired");
				return;
			} catch (Exception e) {
//				throw new BadCredentialsException("Invalid Token received!");
				logger.error("--------------JWTTokenValidatorFilter--------------invalid token");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized: Invalid Signature");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getServletPath().equals("/auth/login");
	}
	
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(SecurityConstants.JWT_HEADER);
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}
}