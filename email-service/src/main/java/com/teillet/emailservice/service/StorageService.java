package com.teillet.emailservice.service;

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

	@Value("${minio.bucket-name}")
	private String bucketName;

	private final IMinioStorageService minioStorageService;

	@Override
	public void downloadCorrectedTranscription(String correctedTranscription, File outputPath) {
		minioStorageService.downloadFile(correctedTranscription, outputPath, bucketName, secretKey, minioUrl, accessKey);
	}
}
