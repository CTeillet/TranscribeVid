package com.teillet.transcribeservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teillet.transcribeservice.dto.TranscriptionData;
import com.teillet.transcribeservice.dto.TranscriptionFullResult;
import com.teillet.transcribeservice.dto.TranscriptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranscriptionService implements ITranscriptionService {
	private static final List<String> END_STATUS = List.of("cancelled", "failed", "success");
	private static final String SUCCESS = "success";
	private static final int RETRY_DELAY_MS = 5000;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${transcription.api.url}")
	private String transcriptionUrl;

	@Value("${transcription.result.api.url}")
	private String resultUrl;

	@Value("${transcription.api.product-id}")
	private String productId;

	@Value("${transcription.api.key}")
	private String apiKey;

	@Override
	public String transcribe(File videoPath) {
		String batchId = sendTranscriptionRequest(videoPath);
		log.info("Transcription request sent. Batch ID: {}", batchId);
		String transcriptionResult;
		try {
			transcriptionResult = getTranscriptionResult(batchId);
			log.info("Transcription result received: {}", transcriptionResult);
		} catch (Exception e) {
			log.error("Error retrieving transcription result: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to retrieve transcription result.", e);
		}
		return transcriptionResult;
	}

	private String sendTranscriptionRequest(File file) {
		String url = transcriptionUrl.replace("{product_id}", productId);

		HttpHeaders headers = createHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(file));
		body.add("model", "whisperV2");
		body.add("language", "fr");
		body.add("response_format", "json");

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<TranscriptionResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, TranscriptionResponse.class);
			TranscriptionResponse responseBody = response.getBody();

			if (responseBody == null || responseBody.getBatchId() == null) {
				throw new RuntimeException("Invalid response from transcription API.");
			}

			return responseBody.getBatchId();
		} catch (Exception e) {
			log.error("Error sending transcription request: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to send transcription request.", e);
		}
	}

	private String getTranscriptionResult(String batchId) {
		log.info("0 - Transcription request sent. Batch ID: {}", batchId);
		String url = resultUrl.replace("{product_id}", productId).replace("{batch_id}", batchId);

		int attempts = 0;
		boolean isCompleted = false;

		log.info("1 - Transcription result received: {}", url);
		while (!isCompleted && attempts < 100) { // Limite de tentatives configurable
			attempts++;
			try {
				log.info("Checking transcription result. Batch ID: {}, attempt: {}", batchId, attempts);
				ResponseEntity<TranscriptionFullResult> response = restTemplate.exchange(
						url, HttpMethod.GET, new HttpEntity<>(createHeaders()), TranscriptionFullResult.class);

				TranscriptionFullResult result = response.getBody();

				if (result != null && END_STATUS.contains(result.getStatus())) {
					if (SUCCESS.equals(result.getStatus())) {
						try {
							TranscriptionData transcriptionData = objectMapper.readValue(result.getData(), TranscriptionData.class);
							log.info("Transcription successful after {} attempts.", attempts);
							return transcriptionData.getText();
						} catch (JsonProcessingException e) {
							log.error("Failed to parse transcription data.", e);
							throw new RuntimeException("Failed to parse transcription data.", e);
						}
					} else {
						log.error("Transcription failed or cancelled. Status: {}", result.getStatus());
						throw new RuntimeException("Transcription failed or cancelled: " + result.getStatus());
					}
				} else {
					log.info("Attempt {}: Transcription still in progress.", attempts);
				}

				Thread.sleep(RETRY_DELAY_MS);
			} catch (Exception e) {
				log.error("Error retrieving transcription result: {}", e.getMessage(), e);
				throw new RuntimeException("Failed to retrieve transcription result.", e);
			}
		}

		// Si la limite de tentatives est atteinte
		log.error("Transcription result not completed after maximum attempts.");
		throw new RuntimeException("Transcription result not completed in time.");
	}


	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(apiKey);
		return headers;
	}

}
