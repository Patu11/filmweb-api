package com.github.patu11.filmwebapi.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

public record Series(@Id String url, String title, String photoUrl, float rating, String description,
					 List<Season> seasons) implements Serializable {
}
