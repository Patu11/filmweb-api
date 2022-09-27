package com.github.patu11.filmwebapi.api.service;

import com.github.patu11.filmwebapi.api.exception.LogFilesNotFoundException;
import com.github.patu11.filmwebapi.model.LogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LogService {
	private static final Logger logger = LoggerFactory.getLogger(LogService.class);

	@Value("${logs.directory}")
	private String logsDirectory;

	public List<LogInfo> getLogs() {
		logger.info("Starting getting logs.");
		return getLogFilesNames().stream()
				.map(f -> new LogInfo(f, getFileContent(f)))
				.toList();
	}

	private List<String> getLogFilesNames() {
		try (Stream<Path> paths = Files.walk(Paths.get(logsDirectory), 1)) {
			return paths
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.map(File::getName)
					.filter(file -> file.startsWith("application") && file.endsWith(".log"))
					.toList();
		} catch (IOException e) {
			logger.error("Error while searching log files: " + e.getMessage());
			throw new LogFilesNotFoundException("Error while searching log files.");
		}
	}

	private List<String> getFileContent(String fileName) {
		Path path = Paths.get(logsDirectory + File.separator + fileName);
		try {
			Stream<String> lines = Files.lines(path);
			List<String> content = lines.toList();
			lines.close();
			return content;
		} catch (IOException e) {
			logger.error("Error while getting content for file: " + fileName);
			throw new RuntimeException(e);
		}
	}
}
