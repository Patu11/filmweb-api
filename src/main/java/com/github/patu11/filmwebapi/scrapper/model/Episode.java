package com.github.patu11.filmwebapi.scrapper.model;

import java.io.Serializable;

public record Episode(String title, String premiereDate, float rating, String description) implements Serializable {
}
