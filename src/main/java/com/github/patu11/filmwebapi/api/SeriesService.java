package com.github.patu11.filmwebapi.api;

import com.github.patu11.filmwebapi.api.dto.SeriesRequest;
import com.github.patu11.filmwebapi.scrapper.Scraper;
import com.github.patu11.filmwebapi.scrapper.model.Series;
import org.springframework.stereotype.Service;

@Service
public class SeriesService {

	public Series getSeries(SeriesRequest seriesRequest) {
		return new Scraper(seriesRequest.seriesUrl()).getSeries();
	}
}
