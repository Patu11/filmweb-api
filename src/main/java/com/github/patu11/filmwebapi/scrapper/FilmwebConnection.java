package com.github.patu11.filmwebapi.scrapper;

import com.github.patu11.filmwebapi.api.exception.WrongUrlException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.github.patu11.filmwebapi.scrapper.ScrapingVars.FILMWEB_URL;

@Component
public class FilmwebConnection implements Connection {

	@Override
	public Document connect(String url) {
		validateUrl(url);
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new RuntimeException("Cannot get document");
		}
	}

	private void validateUrl(String url) {
		if (url.isBlank() || !url.startsWith(FILMWEB_URL.toString())) {
			throw new WrongUrlException("Filmweb url is not valid");
		}
	}
}
