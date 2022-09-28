package com.github.patu11.filmwebapi.api.controller;

import com.github.patu11.filmwebapi.api.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
	private final AuthenticationService authenticationService;

	@GetMapping("/{basicToken}")
	public ResponseEntity<String> checkAuth(@PathVariable String basicToken) {
		return new ResponseEntity<>(authenticationService.checkAuth(basicToken));
	}
}
