package com.github.patu11.filmwebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FilmwebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmwebApiApplication.class, args);
	}

}
