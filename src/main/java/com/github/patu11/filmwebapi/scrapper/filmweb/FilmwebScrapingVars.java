package com.github.patu11.filmwebapi.scrapper.filmweb;

public enum FilmwebScrapingVars {
	FILMWEB_URL("https://www.filmweb.pl"),
	HREF("href"),
	PREVIEW_LINK(".preview__link"),
	ITEMPROP("itemprop"),
	DESCRIPTION("DESCRIPTION"),
	FILMRATING_PANEL("filmRating--hasPanel"),
	FILMRATING_RATE_VALUE(".filmRating__rateValue"),
	FILMCOVER_TITLE("filmCoverSection__title"),
	FILMCOVER_ORIGINAL_TITLE("filmCoverSection__originalTitle"),
	FILM_POSTER("filmPoster"),
	CONTENT("content"),
	FILM_HEADER("filmHeaderSection__title"),
	PREVIEW_EPISODE("previewEpisode"),
	PREVIEW_CARD(".preview__card"),
	PREVIEW_HEADER(".preview__header"),
	DESCRIPTION_SECTION("descriptionSection__text"),
	DATA_SOURCE_TITLE("data-source-title"),
	PREVIEW_YEAR(".preview__year"),
	SQUARE_NAV("squareNavigation__item"),
	LINK("link"),
	FILM_POSTER_SECTION("filmPosterSection__plot");

	final String value;

	FilmwebScrapingVars(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
