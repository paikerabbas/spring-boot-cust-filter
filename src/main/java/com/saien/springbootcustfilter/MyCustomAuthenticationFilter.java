package com.saien.springbootcustfilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class MyCustomAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String authToken = req.getHeader("CustomAuth");

		MyCustomAuthentication custAuthentication = new MyCustomAuthentication(authToken, null);
		try {
			Authentication authResult = authenticationManager.authenticate(custAuthentication);
			if (authResult.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(authResult);
				chain.doFilter(request, response);
			} else {
				res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
		} catch (AuthenticationException ex) {
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}

	}

}
