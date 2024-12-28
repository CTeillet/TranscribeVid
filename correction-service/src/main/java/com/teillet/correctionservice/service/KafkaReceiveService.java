package com.teillet.correctionservice.service;

import com.teillet.correctionservice.dto.RecasePunctResult;
import com.teillet.shared.avro.CorrectionRequest;
import com.teillet.shared.avro.MailRequest;
import com.teillet.shared.mapper.IMapper;
import com.teillet.shared.service.IKafkaProducerService;
import com.teillet.shared.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_CORRECT_TEXT_TOPIC;
import static com.teillet.shared.utils.KafkaConstant.KAFKA_SEND_FINAL_TEXT_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaReceiveService {
	private final IRecasePunctService recasePunctService;
	private final IMapper mapper = IMapper.INSTANCE;
	private final IKafkaProducerService<MailRequest> kafkaProducerService;
	private final IStorageService storageService;

	@KafkaListener(topics = KAFKA_CORRECT_TEXT_TOPIC)
	public void consume(CorrectionRequest request) throws Exception {
		log.info("Received Kafka message on topic {}: {}", KAFKA_CORRECT_TEXT_TOPIC, request);
		RecasePunctResult result = recasePunctService.callRecasePuncApi(request.getTranscription().toString());
		log.info("RecasePunct API response: {}", result);

		File correctedTranscriptionDirectory = new File("corrected-transcription");
		if (!correctedTranscriptionDirectory.exists()) {
			boolean dirCreated = correctedTranscriptionDirectory.mkdirs();
			if (dirCreated) {
				log.info("Created download directory: {}", correctedTranscriptionDirectory.getAbsolutePath());
			} else {
				log.warn("Failed to create download directory: {}", correctedTranscriptionDirectory.getAbsolutePath());
			}
		}

		String filename = request.getRequestId() + ".txt";
		File correctedTranscriptionFile = new File(correctedTranscriptionDirectory, filename);
		// Save the corrected transcription to a file
		Files.write(correctedTranscriptionFile.toPath(), result.getFormattedText().getBytes());

		storageService.uploadCorrectedTranscription(correctedTranscriptionFile);

		MailRequest mailRequest = mapper.toMailRequest(request);
		mailRequest.setCorrectedTranscriptionFile(filename);

		FileUtils.deleteFile(correctedTranscriptionFile);

		kafkaProducerService.sendMessage(KAFKA_SEND_FINAL_TEXT_TOPIC, mailRequest);
	}
}
