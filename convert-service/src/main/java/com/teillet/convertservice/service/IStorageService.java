package com.teillet.convertservice.service;

import java.io.File;

public interface IStorageService {
	void downloadVideo(String videoName, File outputFile);
	void uploadAudio(File file) throws Exception;
}
