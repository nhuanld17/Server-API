package com.example.SERVER.config;

import com.example.SERVER.domain.pojo.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
	
	private final ObjectMapper mapper;
	
	public CustomAuthenticationEntryPoint() {
		mapper = new ObjectMapper();
	}
	
	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException)
			throws IOException, ServletException
	{
		this.delegate.commence(request, response, authException);
		
		response.setContentType("application/json;charset=utf-8");
		
		RestResponse<Object> restResponse = new RestResponse<>();
		restResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		
		String errorMessage = Optional.ofNullable(authException.getCause())
				.map(Throwable::getMessage)
				.orElse(authException.getMessage());
		restResponse.setError(errorMessage);
		
		restResponse.setMessage("Token không hợp lệ");
		
		response.getWriter().write(mapper.writeValueAsString(restResponse));
	}
}
