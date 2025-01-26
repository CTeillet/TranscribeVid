package com.teillet.transcribeservice.service;

import java.io.File;

public interface ITranscriptionService {
	/**
	 * Transcribe a video file
	 *
	 * @param videoPath the path of the video file
	 * @return the transcription of the video
	 */
	String transcribe(File videoPath);
}
