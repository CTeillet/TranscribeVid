package com.teillet.shared.utils;

public class KafkaConstant {
	KafkaConstant () {}

	public static final String KAFKA_DOWNLOAD_VIDEO_TOPIC = "video.download";
	public static final String KAFKA_CONVERT_VIDEO_TOPIC = "video.convert";
	public static final String KAFKA_TRANSCRIPTION_AUDIO_TOPIC = "audio.transcription";
	public static final String KAFKA_CORRECT_TEXT_TOPIC = "text.correct";
	public static final String KAFKA_SEND_FINAL_TEXT_TOPIC = "text.send";
}
