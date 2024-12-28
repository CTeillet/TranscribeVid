package com.teillet.downloadservice.service;


import com.teillet.shared.avro.ConversionRequest;
import com.teillet.shared.avro.DownloadRequest;
import com.teillet.shared.mapper.IMapper;
import com.teillet.shared.model.LaunchProcessRequest;
import com.teillet.shared.service.IKafkaProducerService;
import com.teillet.shared.service.ITranscribeRequestService;
import com.teillet.shared.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_CONVERT_VIDEO_TOPIC;
import static com.teillet.shared.utils.KafkaConstant.KAFKA_DOWNLOAD_VIDEO_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaReceiveService {
	private final ITranscribeRequestService transcribeRequestService;
	private final IKafkaProducerService<ConversionRequest> kafkaProducerService;
	private final IVideoDownloadService videoDownloadService;
	private final IStorageService storageService;
	private final IMapper mapper = IMapper.INSTANCE;

	@KafkaListener(topics = KAFKA_DOWNLOAD_VIDEO_TOPIC)
	public void consume(DownloadRequest request) throws Exception {
		log.info("Received Kafka message on topic {}: {}", KAFKA_DOWNLOAD_VIDEO_TOPIC, request);

		try {
			log.debug("Fetching TranscribeRequest for requestId: {}", request.getRequestId());
			LaunchProcessRequest launchProcessRequest = transcribeRequestService.getRequest(request.getRequestId().toString());

			String videoUrl = launchProcessRequest.getVideoUrl();
			log.info("Video URL to process: {}", videoUrl);

			File downloadDirectory = new File("./download-download-service");

			if (!downloadDirectory.exists()) {
				boolean dirCreated = downloadDirectory.mkdirs();
				if (dirCreated) {
					log.info("Created download directory: {}", downloadDirectory.getAbsolutePath());
				} else {
					log.warn("Failed to create download directory: {}", downloadDirectory.getAbsolutePath());
				}
			}

			String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
			File downloadedFile = new File(downloadDirectory, request.getRequestId() + "." +  FilenameUtils.getExtension(new File(fileName).getName()));
			log.info("Output file path set to: {}", downloadedFile.getAbsolutePath());

			log.info("Starting video download for file: {}", fileName);
			videoDownloadService.downloadVideoWithOkHttp(downloadedFile, launchProcessRequest.getVideoUrl());
			log.info("Successfully downloaded video: {}", downloadedFile.getAbsolutePath());

			storageService.uploadVideo(downloadedFile);
			FileUtils.deleteFile(downloadedFile);

			//delete the file after uploading
			if (downloadedFile.exists() && !downloadedFile.delete()) {
				log.warn("Failed to delete the file: {}", downloadedFile.getAbsolutePath());
			} else {
				log.info("Deleted file: {}", downloadedFile.getAbsolutePath());
			}

			ConversionRequest conversionRequest = mapper.toConversionRequest(request);
			conversionRequest.setVideoFile(downloadedFile.getName());

			log.info("Sending Kafka message to topic {}: {}", KAFKA_CONVERT_VIDEO_TOPIC, request);
			kafkaProducerService.sendMessage(KAFKA_CONVERT_VIDEO_TOPIC, conversionRequest);

		} catch (Exception e) {
			log.error("Error processing requestId {}: {}", request.getRequestId(), e.getMessage(), e);
			throw e; // Rethrow to ensure proper handling by Kafka error mechanisms
		}

		log.info("Finished processing requestId: {}", request.getRequestId());
	}

}
