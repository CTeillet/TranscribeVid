package com.teillet.shared.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FileUtils {
	public static void deleteFile(File convertedFile) {
		if (convertedFile.exists() && !convertedFile.delete()) {
			log.warn("Failed to delete the file: {}", convertedFile.getAbsolutePath());
		} else {
			log.info("Deleted the file: {}", convertedFile.getAbsolutePath());
		}
	}
}
