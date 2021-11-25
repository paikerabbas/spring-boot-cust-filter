package com.saien.springbootcustfilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MyCustomAuthenticationFilter implements Filter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String authToken = req.getHeader("CustomAuth");

		MyCustomAuthentication custAuthentication = new MyCustomAuthentication(authToken, null);
		// below code will call the AuthenticationProvider
		Authentication authResult = authenticationManager.authenticate(custAuthentication);

		if (authResult.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authResult);
			chain.doFilter(request, response);
		}

	}

}
