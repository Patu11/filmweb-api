package com.github.patu11.filmwebapi.model;

import java.io.Serializable;
import java.util.List;

public record LogInfo(String fileName, List<String> content) implements Serializable {
}
