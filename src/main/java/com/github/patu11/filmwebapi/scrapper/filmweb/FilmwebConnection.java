package com.github.patu11.filmwebapi.scrapper.filmweb;

import com.github.patu11.filmwebapi.api.exception.FilmwebConnectionException;
import com.github.patu11.filmwebapi.api.exception.WrongUrlException;
import com.github.patu11.filmwebapi.scrapper.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.github.patu11.filmwebapi.scrapper.filmweb.FilmwebScrapingVars.FILMWEB_URL;

@Component("filmwebConnection")
public class FilmwebConnection implements Connection {
	private static final Logger logger = LoggerFactory.getLogger(FilmwebConnection.class);

	@Override
	public Document connect(String url) {
		validateUrl(url);
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error("Exception while getting document", e);
			throw new FilmwebConnectionException("Cannot get document");
		}
	}

	private void validateUrl(String url) {
		if (url.isBlank() || !url.startsWith(FILMWEB_URL.toString())) {
			logger.error("Filmweb url is not valid: " + url);
			throw new WrongUrlException("Filmweb url is not valid");
		}
	}
}
