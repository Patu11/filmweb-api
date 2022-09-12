package com.github.patu11.filmwebapi.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/v3/api-docs/**").permitAll()
				.antMatchers("/swagger-ui/**").permitAll()
				.antMatchers("/docs").permitAll()
				.antMatchers("/swagger-ui/index.html").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.and()
				.httpBasic();

		return http.build();
	}
}
