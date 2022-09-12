package com.github.patu11.filmwebapi.api.service;

import com.github.patu11.filmwebapi.api.dto.SeriesRequest;
import com.github.patu11.filmwebapi.scrapper.SeriesScraper;
import com.github.patu11.filmwebapi.scrapper.model.Series;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SeriesService {
	private static final Logger logger = LoggerFactory.getLogger(SeriesService.class);
	private final SeriesScraper seriesScraper;

	@Cacheable(value = "series")
	public Series getSeries(SeriesRequest seriesRequest) {
		logger.info("Starting scraping for request: {}", seriesRequest);
		return seriesScraper.getSeries(seriesRequest.seriesUrl());
	}
}
