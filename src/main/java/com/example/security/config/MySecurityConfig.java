package com.example.security.config;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.security.filter.AuthoritiesLoggingAfterFilter;
import com.example.security.filter.JWTTokenValidatorFilter;
import com.example.security.filter.RequestValidationBeforeFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,  jsr250Enabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.cors().configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
	//				config.setAllowedOrigins(Collections.singletonList("http://172.16.2.29:3000"));
					config.addAllowedOriginPattern(CorsConfiguration.ALL);
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setMaxAge(3600L);
					return config;
				}
			})
		.and()
			.csrf().disable()
			.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
			.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
			.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
		.authorizeRequests()
			.antMatchers("/myAccount").hasRole("USER")
			.antMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
			.antMatchers("/myLoans").hasRole("ROOT")
			.antMatchers("/myCards").authenticated()
			.antMatchers("/notices").permitAll()
			.antMatchers("/contact").permitAll()
		.and().formLogin().and().httpBasic();
	}

//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
