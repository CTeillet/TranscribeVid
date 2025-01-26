package com.teillet.transcribeservice.service;

import com.teillet.shared.service.IMinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService implements IStorageService {

	@Value("${minio.url}")
	private String minioUrl;

	@Value("${minio.access-key}")
	private String accessKey;

	@Value("${minio.secret-key}")
	private String secretKey;

	@Value("${minio.bucket-name}")
	private String audioBucketName;

	@Value("${minio.transcription-bucket-name}")
	private String transcriptionBucketName;

	private final IMinioStorageService minioStorageService;

	@Override
	public void downloadAudio(String videoName, File outputPath) {
		minioStorageService.downloadFile(videoName, outputPath, audioBucketName, secretKey, minioUrl, accessKey);
	}

	@Override
	public void uploadCorrectedTranscription(File file) throws Exception {
		log.info("Uploading file to MinIO bucket: {}", transcriptionBucketName);
		minioStorageService.uploadFile(file, transcriptionBucketName, secretKey, minioUrl, accessKey);
		log.info("File successfully uploaded to MinIO. Bucket: {}, File: {}", transcriptionBucketName, file);
	}

}
