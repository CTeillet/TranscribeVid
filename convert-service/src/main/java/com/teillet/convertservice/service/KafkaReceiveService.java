package com.teillet.convertservice.service;

import com.teillet.shared.avro.ConversionRequest;
import com.teillet.shared.avro.TranscriptionRequest;
import com.teillet.shared.mapper.IMapper;
import com.teillet.shared.service.IKafkaProducerService;
import com.teillet.shared.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_CONVERT_VIDEO_TOPIC;
import static com.teillet.shared.utils.KafkaConstant.KAFKA_TRANSCRIPTION_AUDIO_TOPIC;
import static org.apache.commons.io.file.PathUtils.deleteFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaReceiveService {
	private final IStorageService storageService;
	private final IKafkaProducerService<TranscriptionRequest> kafkaProducerService;
	private final IMapper mapper = IMapper.INSTANCE;

	@KafkaListener(topics = KAFKA_CONVERT_VIDEO_TOPIC)
	public void consume(ConversionRequest request) throws Exception {
		log.info("Received Kafka message: {}", request);
		File downloadDirectory = new File("./download-convert-service");

		if (!downloadDirectory.exists()) {
			boolean dirCreated = downloadDirectory.mkdirs();
			if (dirCreated) {
				log.info("Created download directory: {}", downloadDirectory.getAbsolutePath());
			} else {
				log.warn("Failed to create download directory: {}", downloadDirectory.getAbsolutePath());
			}
		}

		File downloadFile = new File(downloadDirectory, String.valueOf(request.getVideoFile()));
		storageService.downloadVideo(request.getVideoFile().toString(), downloadFile);

		File convertedFile = new File(downloadDirectory, request.getRequestId().toString() + ".wav");
		AudioVideoConversionUtils.convertVideoToAudio(downloadFile, convertedFile);

		storageService.uploadAudio(convertedFile);
		//delete the file after uploading
		FileUtils.deleteFile(convertedFile);
		FileUtils.deleteFile(downloadFile);

		TranscriptionRequest transcriptionRequest = mapper.toTranscriptionRequest(request);
		transcriptionRequest.setAudioFile(convertedFile.getName());

		kafkaProducerService.sendMessage(KAFKA_TRANSCRIPTION_AUDIO_TOPIC, transcriptionRequest);
	}

}
