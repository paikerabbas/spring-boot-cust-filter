package com.saien.springbootcustfilter;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class MyCustomAuthenticationProvider implements AuthenticationProvider {

	private final String secretKey = "password";

	@Autowired
	private DriverManagerDataSource dataSource;

	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String requestKey = authentication.getName();
		
		dataSource.setUsername("username");
		dataSource.setPassword("password");

		JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);

		JdbcTemplate jdbcTemplate = userManager.getJdbcTemplate();
		List<UserRequest> users = jdbcTemplate.query("select * from users", (rs, rowNum) -> {
			UserRequest user = new UserRequest();
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			return user;
		});
		users.forEach(System.out::println);

		if (requestKey.equals(secretKey)) {
			MyCustomAuthentication fulluAuthenticated = new MyCustomAuthentication(null, null, null);
			return fulluAuthenticated;
		} else {
			throw new BadCredentialsException("Header value is not correct");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return MyCustomAuthentication.class.equals(authentication);
	}

}
