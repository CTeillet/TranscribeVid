package com.teillet.transcribeservice.service;

import com.neovisionaries.ws.client.WebSocketException;
import com.teillet.shared.avro.CorrectionRequest;
import com.teillet.shared.avro.TranscriptionRequest;
import com.teillet.shared.mapper.IMapper;
import com.teillet.shared.service.IKafkaProducerService;
import com.teillet.shared.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_CORRECT_TEXT_TOPIC;
import static com.teillet.shared.utils.KafkaConstant.KAFKA_TRANSCRIPT_AUDIO_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaReceiveService {
	private final IStorageService storageService;
	private final IVoskService voskService;
	private final IKafkaProducerService<CorrectionRequest> kafkaProducerService;
	private final IMapper mapper = IMapper.INSTANCE;

	@KafkaListener(topics = KAFKA_TRANSCRIPT_AUDIO_TOPIC)
	public void consume(TranscriptionRequest request) throws UnsupportedAudioFileException, WebSocketException, IOException, InterruptedException {
		log.info("Received Kafka message on topic {}: {}", KAFKA_TRANSCRIPT_AUDIO_TOPIC, request);
		String string = request.getAudioFile().toString();

		File downloadDirectory = new File("download-transcription-service");

		if (!downloadDirectory.exists()) {
			boolean dirCreated = downloadDirectory.mkdirs();
			if (dirCreated) {
				log.info("Created download directory: {}", downloadDirectory.getAbsolutePath());
			} else {
				log.warn("Failed to create download directory: {}", downloadDirectory.getAbsolutePath());
			}
		}

		File outputPath = new File(downloadDirectory, string);
		storageService.downloadAudio(string, outputPath);

		log.info("Downloaded audio file: {}", outputPath.getAbsolutePath());

		List<String> result = voskService.transcriptFile(outputPath.getAbsolutePath());

		log.info("Transcription result: {}", result);

		FileUtils.deleteFile(outputPath);
		log.info("Deleted audio file: {}", outputPath.getAbsolutePath());

		CorrectionRequest correctionRequest = mapper.toCorrectionRequest(request);
		correctionRequest.setTranscription(String.join("", result));

		kafkaProducerService.sendMessage(KAFKA_CORRECT_TEXT_TOPIC, correctionRequest);
	}
}
