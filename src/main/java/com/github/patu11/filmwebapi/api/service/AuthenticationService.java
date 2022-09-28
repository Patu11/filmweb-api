package com.github.patu11.filmwebapi.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthenticationService {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	@Value("${spring.security.user.name}")
	private String securityUsername;

	@Value("${spring.security.user.password}")
	private String securityPassword;

	public HttpStatus checkAuth(String basicToken) {
		String pair = new String(Base64.getDecoder().decode(basicToken));
		String userName = pair.split(":")[0];
		String password = pair.split(":")[1];
		logger.info("Checking authorization for user: " + userName);
		return userName.equals(securityUsername) && password.equals(securityPassword) ? HttpStatus.ACCEPTED : HttpStatus.UNAUTHORIZED;
	}
}
