package com.github.patu11.filmwebapi.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Series {
	private String title;
	private String photoUrl;
	private List<Season> seasons;
}
