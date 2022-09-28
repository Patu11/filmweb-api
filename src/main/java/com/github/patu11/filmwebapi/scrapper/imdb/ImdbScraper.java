package com.github.patu11.filmwebapi.scrapper.imdb;

import com.github.patu11.filmwebapi.api.exception.WrongUrlException;
import com.github.patu11.filmwebapi.model.Episode;
import com.github.patu11.filmwebapi.model.Season;
import com.github.patu11.filmwebapi.model.Series;
import com.github.patu11.filmwebapi.scrapper.Connection;
import com.github.patu11.filmwebapi.scrapper.Scraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.github.patu11.filmwebapi.scrapper.imdb.ImdbScrapingVars.IMDB_URL;

@Component
public class ImdbScraper implements Scraper {
	private final Logger logger = LoggerFactory.getLogger(ImdbScraper.class);

	private final Connection connection;

	public ImdbScraper(@Qualifier("imdbConnection") Connection connection) {
		this.connection = connection;
	}

	@Override
	public Series getSeries(String seriesUrl) {
		Document seriesDocument = connection.connect(seriesUrl);
		return getSeries(seriesDocument);
	}

	@Override
	public Series getSeries(Document seriesDocument) {
		return new Series(
				getSeriesUrl(seriesDocument),
				getTitle(seriesDocument),
				getPhotoUrl(seriesDocument),
				getRating(seriesDocument),
				getSeriesDescription(seriesDocument),
				getSeasons(seriesDocument));
	}

	private List<Season> getSeasons(Document seriesDocument) {
		logger.info("Getting series seasons.");
		return getSeasonsUrl(seriesDocument).stream()
				.map(this::mapSeason)
				.toList();
	}

	private Season mapSeason(String seasonUrl) {
		Document seasonDocument = connection.connect(seasonUrl);
		int seasonNumber = Character.getNumericValue(seasonUrl.charAt(seasonUrl.length() - 1));

		List<Episode> episodes = seasonDocument.getElementsByClass("list detail eplist").select(".list_item").stream()
				.map(this::mapEpisode)
				.toList();

		return new Season(seasonNumber, episodes);
	}

	private Episode mapEpisode(Element episodeElement) {
		String title = episodeElement.getElementsByAttribute("title").attr("title");
		String premiereDate = episodeElement.getElementsByClass("airdate").text();
		float rating = episodeElement.getElementsByClass("ipl-rating-star__rating").stream()
				.findFirst()
				.map(Element::text)
				.map(Float::parseFloat)
				.orElse(-1.0f);
		String description = episodeElement.getElementsByClass("item_description").text();
		return new Episode(title, premiereDate, rating, description);
	}

	private List<String> getSeasonsUrl(Document seriesDocument) {
		Document allEpisodes = getAllEpisodesDocument(seriesDocument);
		String seasonsPatterns = "?season=";
		String seasonUrl = allEpisodes.getElementsByAttributeValue("rel", "canonical").attr("href");
		return Objects.requireNonNull(allEpisodes.getElementById("bySeason")).getElementsByAttribute("value").stream()
				.map(el -> el.attr("value"))
				.map(el -> seasonUrl + seasonsPatterns + el)
				.toList();
	}

	private Document getAllEpisodesDocument(Document seriesDocument) {
		String allEpisodesUrl = seriesDocument.getElementsByAttributeValue("cel_widget_id", "DynamicFeature_Episodes").stream()
				.findFirst()
				.map(el -> el.getElementsByAttribute("href"))
				.map(el -> el.attr("href"))
				.orElseThrow(() -> new WrongUrlException("Imdb url is not valid"));

		return connection.connect(IMDB_URL + allEpisodesUrl);
	}

	private String getSeriesDescription(Document seriesDocument) {
		logger.info("Getting series description.");
		return seriesDocument.getElementsByClass("sc-16ede01-2").stream()
				.findFirst()
				.map(Element::text)
				.orElseGet(String::new);
	}

	private float getRating(Document seriesDocument) {
		logger.info("Getting series rating.");
		return seriesDocument.getElementsByClass("sc-7ab21ed2-1").stream()
				.findFirst()
				.map(Element::text)
				.map(Float::parseFloat)
				.orElse(-1.0f);
	}

	private String getPhotoUrl(Document seriesDocument) {
		logger.info("Getting series photo url.");
		return seriesDocument.getElementsByAttributeValue("property", "og:image").stream()
				.findFirst()
				.map(el -> el.attr("content"))
				.orElseGet(String::new);
	}

	private String getTitle(Document seriesDocument) {
		logger.info("Getting series title.");
		return seriesDocument.getElementsByClass("sc-dae4a1bc-0").stream()
				.findFirst()
				.map(Element::text)
				.map(title -> title.substring(16))
				.orElseGet(String::new);
	}

	private String getSeriesUrl(Document seriesDocument) {
		logger.info("Getting series url.");
		return seriesDocument.getElementsByAttributeValue("property", "og:url").stream()
				.findFirst()
				.map(el -> el.attr("content"))
				.orElseGet(String::new);
	}
}
