package com.teillet.gatewayservice.service;

import com.teillet.shared.avro.DownloadRequest;
import com.teillet.shared.model.LaunchProcessRequest;
import com.teillet.shared.service.ITranscribeRequestService;
import com.teillet.gatewayservice.dto.TranscribeRequestDto;
import com.teillet.shared.service.IKafkaProducerService;
import com.teillet.gatewayservice.mapper.TranscribeRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_DOWNLOAD_VIDEO_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaunchProcess implements ILaunchProcess {
	private final ITranscribeRequestService transcribeRequestService;
	private final IKafkaProducerService<DownloadRequest>  kafkaProducerService;

	public LaunchProcessRequest launchProcess(TranscribeRequestDto requestDto) {
		log.info("Launch process");
		// Transforme le DTO en entité
		LaunchProcessRequest request = TranscribeRequestMapper.INSTANCE.toEntity(requestDto);

		log.info("Save the entity");
		// Sauvegarde l'entité dans la base de données
		LaunchProcessRequest savedRequest = transcribeRequestService.saveRequest(request);

		Instant timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
		DownloadRequest downloadRequest = DownloadRequest.newBuilder().setRequestId(savedRequest.getRequestId()).setTimestamp(timestamp).build();

		log.info("Send kafka message : downloadRequest {}", downloadRequest);
		kafkaProducerService.sendMessage(KAFKA_DOWNLOAD_VIDEO_TOPIC, downloadRequest);

		// Retourne une réponse avec l'entité sauvegardée
		return savedRequest;
	}
}
