package com.github.patu11.filmwebapi.scrapper;

import com.github.patu11.filmwebapi.model.Series;
import org.jsoup.nodes.Document;

public interface Scraper {
	Series getSeries(String seriesUrl);

	Series getSeries(Document seriesDocument);
}
