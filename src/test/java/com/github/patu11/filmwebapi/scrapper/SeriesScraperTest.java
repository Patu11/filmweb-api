package com.github.patu11.filmwebapi.scrapper;

import com.github.patu11.filmwebapi.model.Episode;
import com.github.patu11.filmwebapi.model.Series;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeriesScraperTest {
	private static final String URL = "https://www.filmweb.pl/serial/Limitless-2015-742527";
	private static final String TITLE = "Limitless";
	private static final String PHOTO_URL = "https://fwcdn.pl/fpo/25/27/742527/7697809.3.jpg";
	private static final float RATING = 7.3f;
	private static final String DESCRIPTION = "Brian po zażyciu eksperymentalnego narkotyku zaczyna w pełni wykorzystywać możliwości swojego mózgu.";
	private static final int NUMBER_OF_SEASONS = 1;
	private static final int NUMBER_OF_EPISODES = 22;
	private static final String FIRST_EPISODE_TITLE = "Pilot";
	private static final String FIRST_EPISODE_PREMIERE_DATE = "2015-09-22";
	private static final float FIRST_EPISODE_RATING = 7.9f;

	private static Series series;

	@BeforeAll
	public static void setup() {
		series = new SeriesScraper(new FilmwebConnection()).getSeries(URL);
	}

	@Test
	public void shouldHaveCorrectUrl() {
		assertEquals(URL, series.url());
	}

	@Test
	public void shouldHaveCorrectTitle() {
		assertEquals(TITLE, series.title());
	}

	@Test
	public void shouldHaveCorrectPhotoUrl() {
		assertEquals(PHOTO_URL, series.photoUrl());
	}

	@Test
	public void shouldHaveCorrectRating() {
		assertEquals(RATING, series.rating());
	}

	@Test
	public void shouldHaveCorrectDescription() {
		assertEquals(DESCRIPTION, series.description());
	}

	@Test
	public void shouldHaveCorrectNumberOfSeasons() {
		assertEquals(NUMBER_OF_SEASONS, series.seasons().size());
	}

	@Test
	public void shouldHaveCorrectNumberOfEpisodesInFirstSeason() {
		assertEquals(NUMBER_OF_EPISODES, series.seasons().get(0).episodes().size());
	}

	@Test
	public void firstEpisodeShouldHaveCorrectTitle() {
		Episode firstEpisode = series.seasons().get(0).episodes().get(0);
		assertEquals(FIRST_EPISODE_TITLE, firstEpisode.title());
	}

	@Test
	public void firstEpisodeShouldHaveCorrectPremiereDate() {
		Episode firstEpisode = series.seasons().get(0).episodes().get(0);
		assertEquals(FIRST_EPISODE_PREMIERE_DATE, firstEpisode.premiereDate());
	}

	@Test
	public void firstEpisodeShouldHaveCorrectRating() {
		Episode firstEpisode = series.seasons().get(0).episodes().get(0);
		assertEquals(FIRST_EPISODE_RATING, firstEpisode.rating());
	}
}