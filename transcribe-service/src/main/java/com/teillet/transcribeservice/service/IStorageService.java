package com.teillet.transcribeservice.service;

import java.io.File;

public interface IStorageService {
	void downloadAudio(String audioFile, File outputPath);

	void uploadCorrectedTranscription(File file) throws Exception;
}
