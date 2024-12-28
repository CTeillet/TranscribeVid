package com.teillet.emailservice.service;

import com.teillet.shared.avro.MailRequest;
import com.teillet.shared.model.LaunchProcessRequest;
import com.teillet.shared.service.ITranscribeRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_SEND_FINAL_TEXT_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaReceiveService {
	private final IEmailService emailService;
	private final ITranscribeRequestService transcribeRequestService;
	private final IStorageService storageService;


	@KafkaListener(topics = KAFKA_SEND_FINAL_TEXT_TOPIC)
	public void consume(MailRequest request) {
		log.info("Received Kafka message: {}", request);

		File downloadDirectory = new File("download-corrected-transcription");
		if (!downloadDirectory.exists()) {
			boolean dirCreated = downloadDirectory.mkdirs();
			if (dirCreated) {
				log.info("Created download directory: {}", downloadDirectory.getAbsolutePath());
			} else {
				log.warn("Failed to create download directory: {}", downloadDirectory.getAbsolutePath());
			}
		}
		String filename = request.getCorrectedTranscriptionFile().toString();
		File outputPath = new File(downloadDirectory, filename);
		storageService.downloadCorrectedTranscription(filename, outputPath);

		LaunchProcessRequest launchProcessRequest = transcribeRequestService.getRequest(request.getRequestId().toString());

		emailService.sendEmailWithAttachment(launchProcessRequest.getEmail(), "Transcription de la vidéo : " + launchProcessRequest.getVideoUrl(), "Bonjour,\n\nVous trouverez en pièce jointe la transcription de la vidéo que vous avez demandée.\n\nCordialement,\nL'équipe de transcription", outputPath);
	}
}
