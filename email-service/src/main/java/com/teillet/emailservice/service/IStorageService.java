package com.teillet.emailservice.service;

import java.io.File;

public interface IStorageService {
	void downloadCorrectedTranscription(String correctedTranscription, File outputPath);
}
