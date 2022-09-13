package com.github.patu11.filmwebapi.scrapper;

import com.github.patu11.filmwebapi.scrapper.model.Episode;
import com.github.patu11.filmwebapi.scrapper.model.Season;
import com.github.patu11.filmwebapi.scrapper.model.Series;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class SeriesScraper {
	private static final String FILMWEB_URL = "https://www.filmweb.pl";

	public Series getSeries(String seriesUrl) {
		Document seriesDocument = getDocumentFromUrl(seriesUrl);
		return new Series(seriesUrl, getTitle(seriesDocument), getPhotoUrl(seriesDocument), getRating(seriesDocument), getSeriesDescription(seriesDocument), getSeasons(seriesDocument));
	}

	private String getSeriesDescription(Document seriesDocument) {
		Elements descriptionSection = seriesDocument.getElementsByClass("filmPosterSection__plot");
		return descriptionSection.stream()
				.findFirst()
				.map(el -> el.getElementsByAttributeValue("itemprop", "description"))
				.map(Elements::text)
				.orElseGet(String::new);
	}

	private float getRating(Document seriesDocument) {
		Elements ratingPanel = seriesDocument.getElementsByClass("filmRating--hasPanel");
		String rating = ratingPanel.select(".filmRating__rateValue").text();
		return Float.parseFloat(rating.replace(",", "."));
	}

	private List<Season> getSeasons(Document seriesDocument) {
		return getSeasonsUrls(seriesDocument).stream()
				.map(this::mapSeason)
				.toList();
	}

	private String getTitle(Document seriesDocument) {
		String largeTitle = seriesDocument.getElementsByClass("filmCoverSection__title").stream()
				.findFirst()
				.map(Element::text)
				.orElseGet(String::new);

		return seriesDocument.getElementsByClass("filmCoverSection__originalTitle").stream()
				.findFirst()
				.map(Element::text)
				.orElse(largeTitle);
	}

	private String getPhotoUrl(Document seriesDocument) {
		return Optional.ofNullable(seriesDocument.getElementById("filmPoster"))
				.map(img -> img.attr("content"))
				.orElseThrow(() -> new RuntimeException("Photo not found"));
	}

	private Season mapSeason(String seasonUrl) {
		return new Season(getSeasonNumber(seasonUrl), getSeasonEpisodes(seasonUrl));
	}

	private int getSeasonNumber(String seasonUrl) {
		Document seasonDoc = getDocumentFromUrl(seasonUrl);
		return seasonDoc.getElementsByClass("filmHeaderSection__title")
				.stream()
				.findFirst()
				.map(Element::text)
				.map(this::mapSeasonNumber)
				.orElse(-1);
	}

	private int mapSeasonNumber(String seasonTitle) {
		String number = seasonTitle.split(":")[0].split("\\s+")[1];
		return Integer.parseInt(number);
	}

	private List<Episode> getSeasonEpisodes(String seasonUrl) {
		Document seasonDoc = getDocumentFromUrl(seasonUrl);
		Elements episodes = seasonDoc.getElementsByClass("previewEpisode");
		Elements cards = episodes.select(".preview__card");

		return cards.stream()
				.map(this::mapEpisode)
				.toList();
	}

	private Episode mapEpisode(Element previewCard) {
		Elements previewLink = previewCard.select(".preview__link");
		Elements previewContent = previewCard.select(".preview__content");
		Elements previewHeader = previewCard.select(".preview__header");

		String title = getEpisodeTitle(previewLink);
		String date = getEpisodeDate(previewLink);
		float rating = getEpisodeRating(previewContent);
		String description = getEpisodeDescription(previewHeader);

		return new Episode(title, date, rating, description);
	}

	private String getEpisodeDescription(Elements previewHeader) {
		Elements previewLink = previewHeader.select(".preview__link");
		Optional<String> episodeUrl = previewLink.stream()
				.findFirst()
				.map(el -> el.attr("href"));

		if (episodeUrl.isEmpty()) {
			return "";
		}

		Document episodeDocument = getDocumentFromUrl(FILMWEB_URL + episodeUrl.get());

		return episodeDocument.getElementsByClass("descriptionSection__text").stream()
				.findFirst()
				.map(Element::text)
				.orElseGet(String::new);
	}

	private float getEpisodeRating(Elements previewContent) {
		String episodeUrl = previewContent.stream()
				.findFirst()
				.map(el -> el.getElementsByAttribute("href").attr("href"))
				.orElseGet(String::new);

		Document episodeDoc = getDocumentFromUrl(FILMWEB_URL + episodeUrl);

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
				.map(el -> el.getElementsByAttribute("data-source-title").text())
				.orElseGet(String::new);
	}

	private String getEpisodeDate(Elements previewLink) {
		return previewLink.select(".preview__year").text();
	}

	private List<String> getSeasonsUrls(Document seriesDocument) {
		return seriesDocument.getElementsByClass("squareNavigation__item").stream()
				.map(el -> el.attr("href"))
				.map(url -> FILMWEB_URL + url)
				.toList();
	}

	private Document getDocumentFromUrl(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new RuntimeException("Cannot get document");
		}
	}
}
