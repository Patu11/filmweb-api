package com.github.patu11.filmwebapi.api;

import com.github.patu11.filmwebapi.api.dto.SeriesRequest;
import com.github.patu11.filmwebapi.scrapper.model.Series;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class SeriesController {
	private final SeriesService seriesService;

	@PostMapping("/series")
	public Series getSeries(@RequestBody SeriesRequest seriesRequest) {
		return seriesService.getSeries(seriesRequest);
	}
}
