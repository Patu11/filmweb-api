package com.github.patu11.filmwebapi.scrapper.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

public record Series(String title, String photoUrl, @Id String url, float rating,
					 List<Season> seasons) implements Serializable {
}
