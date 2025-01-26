package com.teillet.transcribeservice.service;

import com.teillet.shared.avro.MailRequest;
import com.teillet.shared.avro.TranscriptionRequest;
import com.teillet.shared.mapper.IMapper;
import com.teillet.shared.service.IKafkaProducerService;
import com.teillet.shared.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_SEND_FINAL_TEXT_TOPIC;
import static com.teillet.shared.utils.KafkaConstant.KAFKA_TRANSCRIPT_AUDIO_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaReceiveService {
	private final IStorageService storageService;
	private final IKafkaProducerService<MailRequest> kafkaProducerService;
	private final ITranscriptionService transcriptionService;
	private final IMapper mapper = IMapper.INSTANCE;

	@KafkaListener(topics = KAFKA_TRANSCRIPT_AUDIO_TOPIC)
	public void consume(TranscriptionRequest request) throws Exception {
		log.info("Received Kafka message on topic {}: {}", KAFKA_TRANSCRIPT_AUDIO_TOPIC, request);
		String string = request.getAudioFile().toString();

		File downloadDirectory = getDirectory("download-audio");

		File outputPath = new File(downloadDirectory, string);
		storageService.downloadAudio(string, outputPath);

		log.info("Downloaded audio file: {}", outputPath.getAbsolutePath());

		String transcriptionResult = transcriptionService.transcribe(outputPath);

		log.info("Transcription result: {}", transcriptionResult);

		FileUtils.deleteFile(outputPath);
		log.info("Deleted audio file: {}", outputPath.getAbsolutePath());


		File correctedTranscriptionDirectory = getDirectory("transcription");

		String filename = request.getRequestId() + ".txt";
		File correctedTranscriptionFile = new File(correctedTranscriptionDirectory, filename);
		// Save the corrected transcription to a file
		Files.write(correctedTranscriptionFile.toPath(), transcriptionResult.getBytes());

		storageService.uploadCorrectedTranscription(correctedTranscriptionFile);

		MailRequest mailRequest = mapper.toMailRequest(request);
		mailRequest.setCorrectedTranscriptionFile(filename);

		FileUtils.deleteFile(correctedTranscriptionFile);

		kafkaProducerService.sendMessage(KAFKA_SEND_FINAL_TEXT_TOPIC, mailRequest);
	}

	@NotNull
	private static File getDirectory(String pathname) {
		File downloadDirectory = new File(pathname);

		if (!downloadDirectory.exists()) {
			boolean dirCreated = downloadDirectory.mkdirs();
			if (dirCreated) {
				log.info("Created download directory: {}", downloadDirectory.getAbsolutePath());
			} else {
				log.warn("Failed to create download directory: {}", downloadDirectory.getAbsolutePath());
			}
		}
		return downloadDirectory;
	}
}
