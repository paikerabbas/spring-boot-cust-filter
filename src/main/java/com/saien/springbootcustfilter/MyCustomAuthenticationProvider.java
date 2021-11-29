package com.saien.springbootcustfilter;

import java.sql.Connection;
import java.sql.SQLException;
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
		UserVO userinRequest = (UserVO) authentication.getDetails();
//		.getName();

		dataSource.setUsername(userinRequest.getUsername());
		dataSource.setPassword(userinRequest.getPassword());

		JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);

		JdbcTemplate jdbcTemplate = userManager.getJdbcTemplate();
		List<UserVO> users = jdbcTemplate.query("select * from users", (rs, rowNum) -> {
			UserVO user = new UserVO();
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			return user;
		});
		users.forEach(System.out::println);

		MyCustomAuthentication fulluAuthenticated = null;

		try {
			Connection connection = dataSource.getConnection();
			// If connection is successful, means user is authenticated
			System.out.println("User is authenticated..");
			fulluAuthenticated = new MyCustomAuthentication(null, null, null);

		} catch (SQLException ex) {
			throw new BadCredentialsException("User not authenticated.");
		}

		return fulluAuthenticated;
//		if (requestKey.equals(secretKey)) {
//			MyCustomAuthentication fulluAuthenticated = new MyCustomAuthentication(null, null, null);
//			return fulluAuthenticated;
//		} else {
//			throw new BadCredentialsException("Header value is not correct");
//		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return MyCustomAuthentication.class.equals(authentication);
	}

}
