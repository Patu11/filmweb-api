package com.github.patu11.filmwebapi.scrapper.model;

import java.util.List;


public record Series(String title, String photoUrl, List<Season> seasons) {
}
