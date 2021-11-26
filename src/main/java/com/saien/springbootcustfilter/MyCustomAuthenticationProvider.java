package com.saien.springbootcustfilter;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MyCustomAuthenticationProvider implements AuthenticationProvider {

	private final String secretKey = "password";

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String requestKey = authentication.getName();
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
