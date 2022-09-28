package com.github.patu11.filmwebapi.scrapper.imdb;

public enum ImdbScrapingVars {
	IMDB_URL("https://www.imdb.com/");

	final String value;

	ImdbScrapingVars(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
