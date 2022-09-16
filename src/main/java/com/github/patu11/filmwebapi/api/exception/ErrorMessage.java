package com.github.patu11.filmwebapi.api.exception;

import java.time.LocalDateTime;

public record ErrorMessage(int status, LocalDateTime time, String message) {
}
