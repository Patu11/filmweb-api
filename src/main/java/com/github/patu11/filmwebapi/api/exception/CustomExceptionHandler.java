package com.github.patu11.filmwebapi.api.exception;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(value = {WrongUrlException.class})
	public ResponseEntity<ErrorMessage> wrongUrlException(WrongUrlException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = {FilmwebConnectionException.class})
	public ResponseEntity<ErrorMessage> filmwebConnectionException(FilmwebConnectionException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = {LogFilesNotFoundException.class})
	public ResponseEntity<ErrorMessage> logFilesNotFoundException(LogFilesNotFoundException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = {NotImplementedException.class})
	public ResponseEntity<ErrorMessage> notImplementedException(NotImplementedException ex) {
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage());
		return new ResponseEntity<>(message, HttpStatus.NOT_IMPLEMENTED);
	}
}
