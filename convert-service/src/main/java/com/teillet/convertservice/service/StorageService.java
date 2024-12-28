package com.teillet.convertservice.service;

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

	@Value("${minio.url}")
	private String minioUrl;

	@Value("${minio.access-key}")
	private String accessKey;

	@Value("${minio.secret-key}")
	private String secretKey;

	@Value("${minio.bucket-name-video}")
	private String videoBucketName;

	@Value("${minio.bucket-name-audio}")
	private String audioBucketName;

	private final IMinioStorageService minioStorageService;

	@Override
	public void downloadVideo(String videoName, File outputPath) {
		minioStorageService.downloadFile(videoName,outputPath, videoBucketName, secretKey, minioUrl, accessKey);
	}

	@Override
	public void uploadAudio(File file) throws Exception {
		log.info("Uploading file to MinIO bucket: {}", audioBucketName);
		minioStorageService.uploadFile(file, audioBucketName, secretKey, minioUrl, accessKey);
		log.info("File successfully uploaded to MinIO. Bucket: {}, File: {}", audioBucketName, file);
	}
}
