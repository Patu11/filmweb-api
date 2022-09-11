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
		return new Series(getTitle(seriesDocument), getPhotoUrl(seriesDocument), seriesUrl, getSeasons(seriesDocument));
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

		Elements episodesInfo = episodes.select(".preview__link");

		return episodesInfo.stream()
				.map(this::mapEpisode)
				.toList();
	}

	private Episode mapEpisode(Element element) {
		Elements children = element.children();
		String title = children.stream()
				.filter(el -> el.hasAttr("data-source-title"))
				.findFirst()
				.map(Element::text)
				.orElseGet(String::new);
		String date = children.select(".preview__year").text();

		return new Episode(title, date);
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