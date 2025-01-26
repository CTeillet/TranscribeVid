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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

		String transcriptionResult = getTranscriptionResultAsync(batchId).join();
		log.info("Transcription result received: {}", transcriptionResult);

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

	private CompletableFuture<String> getTranscriptionResultAsync(String batchId) {
		String url = resultUrl.replace("{product_id}", productId).replace("{batch_id}", batchId);

		CompletableFuture<String> resultFuture = new CompletableFuture<>();

		// Utilisation d'un try-with-resources pour gérer le scheduler
		try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1)) {
			Runnable task = new Runnable() {
				int attempts = 0;

				@Override
				public void run() {
					attempts++;
					try {
						ResponseEntity<TranscriptionFullResult> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders()), TranscriptionFullResult.class);

						TranscriptionFullResult result = response.getBody();
						if (result != null && END_STATUS.contains(result.getStatus())) {
							if (SUCCESS.equals(result.getStatus())) {
								try {
									TranscriptionData transcriptionData = objectMapper.readValue(result.getData(), TranscriptionData.class);
									log.info("Transcription successful after {} attempts.", attempts);
									resultFuture.complete(transcriptionData.getText());
								} catch (JsonProcessingException e) {
									log.error("Failed to parse transcription data.", e);
									resultFuture.completeExceptionally(new RuntimeException("Failed to parse transcription data", e));
								}
							} else {
								log.error("Transcription failed or cancelled after {} attempts. Status: {}", attempts, result.getStatus());
								resultFuture.completeExceptionally(new RuntimeException("Transcription failed or cancelled: " + result.getStatus()));
							}
							scheduler.shutdown(); // Stop the scheduler
						} else {
							log.info("Attempt {}: Transcription still in progress.", attempts);
						}
					} catch (Exception e) {
						log.error("Error retrieving transcription result: {}", e.getMessage(), e);
						resultFuture.completeExceptionally(new RuntimeException("Failed to retrieve transcription result.", e));
						scheduler.shutdown(); // Stop the scheduler on error
					}
				}
			};

			// Planification de la tâche
			scheduler.scheduleAtFixedRate(task, 0, RETRY_DELAY_MS, TimeUnit.MILLISECONDS);

			// Shutdown propre après exécution
			scheduler.shutdown();
			boolean terminated = scheduler.awaitTermination(1, TimeUnit.HOURS);
			if (!terminated) {
				log.error("Scheduler did not terminate within the timeout.");
				resultFuture.completeExceptionally(new RuntimeException("Scheduler did not terminate within the timeout."));
			}
		} catch (InterruptedException e) {
			log.error("Scheduler interrupted during shutdown.", e);
			resultFuture.completeExceptionally(new RuntimeException("Scheduler interrupted during shutdown.", e));
		}

		return resultFuture;
	}


	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(apiKey);
		return headers;
	}

}
