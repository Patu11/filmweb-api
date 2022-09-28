package com.github.patu11.filmwebapi.api.service;

import com.github.patu11.filmwebapi.api.dto.SeriesRequest;
import com.github.patu11.filmwebapi.model.Series;
import com.github.patu11.filmwebapi.scrapper.Scraper;
import com.github.patu11.filmwebapi.scrapper.filmweb.FilmwebScraper;
import com.github.patu11.filmwebapi.scrapper.imdb.ImdbScraper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeriesService {
	private static final Logger logger = LoggerFactory.getLogger(SeriesService.class);
	private List<Scraper> scrappers;

	@Cacheable(value = "series")
	public Series getSeries(SeriesRequest seriesRequest, String service) {
		logger.info("Starting scraping for url: {}", seriesRequest.seriesUrl());
		return getProperService(service).getSeries(seriesRequest.seriesUrl());
	}

	private Scraper getProperService(String service) {
		logger.info("Starting to get '{}' service.", service);
		Scraper scraper = switch (service) {
			case "imdb" -> scrappers.stream()
					.filter(connection -> connection instanceof ImdbScraper)
					.findFirst()
					.orElseThrow(() -> logAndThrow(service));
			case "filmweb" -> scrappers.stream()
					.filter(connection -> connection instanceof FilmwebScraper)
					.findFirst()
					.orElseThrow(() -> logAndThrow(service));
			default -> throw logAndThrow(service);
		};

		logger.info("'{}' service successfully obtained.", service);
		return scraper;
	}

	private NotImplementedException logAndThrow(String service) {
		String pattern = "Service '{}' not implemented.";
		logger.error(pattern, service);
		return new NotImplementedException(String.format(pattern, service));
	}
}
