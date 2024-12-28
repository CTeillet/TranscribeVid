package com.teillet.convertservice.service;

import java.io.File;

public interface IAudioVideoConversionService {
	void convertVideoToAudio(File videoFile, File outputAudioFile);
}
