package com.teillet.correctionservice.service;

import com.teillet.shared.service.IMinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageService implements IStorageService {
	private final IMinioStorageService minioStorageService;

	@Value("${minio.url}")
	private String minioUrl;

	@Value("${minio.access-key}")
	private String accessKey;

	@Value("${minio.secret-key}")
	private String secretKey;

	@Value("${minio.bucket-name}")
	private String bucketName;

	@Override
	public void uploadCorrectedTranscription(File file) throws Exception {
		log.info("Uploading file to MinIO bucket: {}", bucketName);
		minioStorageService.uploadFile(file, bucketName, secretKey, minioUrl, accessKey);
		log.info("File successfully uploaded to MinIO. Bucket: {}, File: {}", bucketName, file);
	}
}
