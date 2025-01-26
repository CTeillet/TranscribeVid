package com.teillet.transcribeservice.service;

import java.io.File;

public interface ITranscriptionService {
	String transcribe(File videoPath);
}
