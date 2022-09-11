package com.github.patu11.filmwebapi.scrapper.model;

import java.io.Serializable;

public record Episode(String title, String premiereDate) implements Serializable {
}
