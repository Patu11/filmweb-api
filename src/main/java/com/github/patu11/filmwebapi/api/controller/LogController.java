package com.github.patu11.filmwebapi.api.controller;

import com.github.patu11.filmwebapi.api.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/logs")
public class LogController {
	private final LogService logService;

	@GetMapping
	public Map<String, String> getLogs() {
		return this.logService.getLogs();
	}
}

