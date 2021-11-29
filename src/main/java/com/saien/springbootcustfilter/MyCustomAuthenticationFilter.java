package com.saien.springbootcustfilter;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MyCustomAuthenticationFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String authToken = req.getHeader("CustomAuth");
		
		String body = IOUtils.toString(req.getReader());
		UserVO user = objectMapper.readValue(body, UserVO.class);
		

		MyCustomAuthentication custAuthentication = new MyCustomAuthentication(authToken, null);
		custAuthentication.setDetails(user);
		
		try {
			// below code will call the authenticate() method of AuthenticationProvider
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
