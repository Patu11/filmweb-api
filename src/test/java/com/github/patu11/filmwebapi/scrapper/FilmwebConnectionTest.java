package com.github.patu11.filmwebapi.scrapper;

import com.github.patu11.filmwebapi.api.exception.WrongUrlException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmwebConnectionTest {
	private static final String INVALID_URL = "URL";
	private final FilmwebConnection filmwebConnection = new FilmwebConnection();

	@Test
	public void shouldThrowInvalidUrlError() {
		Exception exception = assertThrows(WrongUrlException.class, () -> filmwebConnection.connect(INVALID_URL));

		String expectedMessage = "Filmweb url is not valid";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
}