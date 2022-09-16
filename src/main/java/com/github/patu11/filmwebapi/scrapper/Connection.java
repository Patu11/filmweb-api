package com.github.patu11.filmwebapi.scrapper;

import org.jsoup.nodes.Document;

public interface Connection {
	Document connect(String url);
}
