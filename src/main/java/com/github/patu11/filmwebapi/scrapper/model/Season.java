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
public class Season {
	private int number;
	List<Episode> episodes;
}
