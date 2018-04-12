package com.datastax.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileUtils {

	private static final String RESOURCES_DIR = "src/main/resources";
	public static List<String> readFileIntoList(String filename) {
		try {
			return Files.readAllLines(Paths.get(RESOURCES_DIR, filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	public static String readFileIntoString(String filename) {
		Path path = Paths.get(RESOURCES_DIR, filename);
		StringBuilder buffer = new StringBuilder();
		try (BufferedReader br = Files.newBufferedReader(path)) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				buffer.append(currentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
