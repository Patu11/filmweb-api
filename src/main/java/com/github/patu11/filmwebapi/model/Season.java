package com.github.patu11.filmwebapi.model;

import java.io.Serializable;
import java.util.List;


public record Season(int number, List<Episode> episodes) implements Serializable {
}
