package com.Shop.Security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {

	private String message;
	private int status;
	public CustomEntryPoint(String mgs, int status) {
		// TODO Auto-generated constructor stub
		this.message=mgs;
		this.status=status;
	}
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		 response.setContentType("application/json;charset=UTF-8");
	        response.setStatus(status);

	        Map<String, Object> error = new HashMap<>();
	        error.put("status", status);
	        error.put("message",message);

	        ObjectMapper mapper = new ObjectMapper();
	        mapper.writeValue(response.getOutputStream(), error);
	}
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

	}


}
