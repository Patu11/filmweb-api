package com.github.patu11.filmwebapi.api.controller;

import com.github.patu11.filmwebapi.api.service.LogService;
import com.github.patu11.filmwebapi.model.LogInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/logs")
public class LogController {
	private final LogService logService;

	@GetMapping
	public List<LogInfo> getLogs() {
		return this.logService.getLogs();
	}
}

