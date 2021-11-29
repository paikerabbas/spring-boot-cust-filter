package com.saien.springbootcustfilter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "Hello User !!";
	}
	
	@PostMapping("/user")
	public String checkUser() {
		return "User Returned!!!!";
	}
	
	@PostMapping("/login")
	public String login() {
		return "User Authenticated!!!";
	}

}
