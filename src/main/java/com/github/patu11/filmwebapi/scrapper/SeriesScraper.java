package com.github.patu11.filmwebapi.scrapper;

import com.github.patu11.filmwebapi.model.Episode;
import com.github.patu11.filmwebapi.model.Season;
import com.github.patu11.filmwebapi.model.Series;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.github.patu11.filmwebapi.scrapper.ScrapingVars.*;

@Component
@AllArgsConstructor
public class SeriesScraper {
	private final Connection connection;

	public Series getSeries(String seriesUrl) {
		Document seriesDocument = connection.connect(seriesUrl);
		return getSeries(seriesDocument);
	}

	public Series getSeries(Document seriesDocument) {
		return new Series(getSeriesUrl(seriesDocument), getTitle(seriesDocument), getPhotoUrl(seriesDocument), getRating(seriesDocument), getSeriesDescription(seriesDocument), getSeasons(seriesDocument));
	}

	private String getSeriesDescription(Document seriesDocument) {
		Elements descriptionSection = seriesDocument.getElementsByClass(FILM_POSTER_SECTION.value);
		return descriptionSection.stream()
				.findFirst()
				.map(el -> el.getElementsByAttributeValue(ITEMPROP.value, DESCRIPTION.value))
				.map(Elements::text)
				.orElseGet(String::new);
	}

	private float getRating(Document seriesDocument) {
		Elements ratingPanel = seriesDocument.getElementsByClass(FILMRATING_PANEL.value);
		String rating = ratingPanel.select(FILMRATING_RATE_VALUE.value).text();
		return Float.parseFloat(rating.replace(",", "."));
	}

	private List<Season> getSeasons(Document seriesDocument) {
		return getSeasonsUrls(seriesDocument).stream()
				.map(this::mapSeason)
				.toList();
	}

	private String getTitle(Document seriesDocument) {
		String largeTitle = seriesDocument.getElementsByClass(FILMCOVER_TITLE.value).stream()
				.findFirst()
				.map(Element::text)
				.orElseGet(String::new);

		return seriesDocument.getElementsByClass(FILMCOVER_ORIGINAL_TITLE.value).stream()
				.findFirst()
				.map(Element::text)
				.orElse(largeTitle);
	}

	private String getPhotoUrl(Document seriesDocument) {
		return Optional.ofNullable(seriesDocument.getElementById(FILM_POSTER.value))
				.map(img -> img.attr(CONTENT.value))
				.orElseGet(String::new);
	}

	private Season mapSeason(String seasonUrl) {
		return new Season(getSeasonNumber(seasonUrl), getSeasonEpisodes(seasonUrl));
	}

	private int getSeasonNumber(String seasonUrl) {
		Document seasonDoc = connection.connect(seasonUrl);
		return seasonDoc.getElementsByClass(FILM_HEADER.value)
				.stream()
				.findFirst()
				.map(Element::text)
				.map(this::mapSeasonNumber)
				.orElse(-1);
	}

	private List<Episode> getSeasonEpisodes(String seasonUrl) {
		Document seasonDoc = connection.connect(seasonUrl);
		Elements episodes = seasonDoc.getElementsByClass(PREVIEW_EPISODE.value);
		Elements cards = episodes.select(PREVIEW_CARD.value);

		return cards.stream()
				.map(this::mapEpisode)
				.toList();
	}

	private int mapSeasonNumber(String seasonTitle) {
		String number = seasonTitle.split(":")[0].split("\\s+")[1];
		return Integer.parseInt(number);
	}

	private Episode mapEpisode(Element previewCard) {
		Elements previewLink = previewCard.select(PREVIEW_LINK.value);
		Elements previewHeader = previewCard.select(PREVIEW_HEADER.value);

		String title = getEpisodeTitle(previewLink);
		String date = getEpisodeDate(previewLink);
		float rating = getEpisodeRating(previewLink);
		String description = getEpisodeDescription(previewHeader);

		return new Episode(title, date, rating, description);
	}

	private String getEpisodeDescription(Elements previewHeader) {
		Elements previewLink = previewHeader.select(PREVIEW_LINK.value);
		Optional<String> episodeUrl = getEpisodeUrl(previewLink);

		if (episodeUrl.isEmpty()) {
			return "";
		}

		Document episodeDocument = connection.connect(FILMWEB_URL + episodeUrl.get());

		return episodeDocument.getElementsByClass(DESCRIPTION_SECTION.value).stream()
				.findFirst()
				.map(Element::text)
				.orElseGet(String::new);
	}

	private float getEpisodeRating(Elements previewLink) {
		String episodeUrl = getEpisodeUrl(previewLink).orElseGet(String::new);

		Document episodeDoc = connection.connect(FILMWEB_URL + episodeUrl);

		return episodeDoc.getElementsByClass("filmRating__rateValue").stream()
				.findFirst()
				.map(Element::text)
				.map(r -> r.replace(",", "."))
				.map(Float::valueOf)
				.orElse(-1.0f);
	}

	private String getEpisodeTitle(Elements previewLink) {
		return previewLink.stream()
				.findFirst()
				.map(el -> el.getElementsByAttribute(DATA_SOURCE_TITLE.value).text())
				.orElseGet(String::new);
	}

	private String getEpisodeDate(Elements previewLink) {
		return previewLink.select(PREVIEW_YEAR.value).text();
	}

	private List<String> getSeasonsUrls(Document seriesDocument) {
		return seriesDocument.getElementsByClass(SQUARE_NAV.value).stream()
				.map(el -> el.attr(HREF.value))
				.map(url -> FILMWEB_URL + url)
				.toList();
	}

	private Optional<String> getEpisodeUrl(Elements previewLink) {
		return previewLink.stream()
				.findFirst()
				.map(el -> el.attr(HREF.value));
	}

	private String getSeriesUrl(Document seriesDocument) {
		return seriesDocument.getElementsByTag(LINK.value)
				.stream()
				.map(el -> el.attr(HREF.value))
				.filter(href -> href.startsWith(FILMWEB_URL + "/serial/"))
				.findFirst()
				.orElseGet(String::new);
	}
}
