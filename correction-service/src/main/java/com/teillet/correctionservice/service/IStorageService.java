package com.teillet.correctionservice.service;

import java.io.File;

public interface IStorageService {
	void uploadCorrectedTranscription(File file) throws Exception;
}
