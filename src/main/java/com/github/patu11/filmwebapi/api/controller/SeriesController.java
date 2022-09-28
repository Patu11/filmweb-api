package com.github.patu11.filmwebapi.api.controller;

import com.github.patu11.filmwebapi.api.dto.SeriesRequest;
import com.github.patu11.filmwebapi.api.service.SeriesService;
import com.github.patu11.filmwebapi.model.Series;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class SeriesController {
	private final SeriesService seriesService;

	@PostMapping("/{service}/series")
	public Series getSeries(@RequestBody SeriesRequest seriesRequest, @PathVariable String service) {
		return seriesService.getSeries(seriesRequest, service);
	}
}
