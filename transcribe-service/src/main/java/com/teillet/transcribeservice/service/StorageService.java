package com.teillet.transcribeservice.service;

import com.teillet.shared.service.IMinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {

	@Value("${minio.url}")
	private String minioUrl;

	@Value("${minio.access-key}")
	private String accessKey;

	@Value("${minio.secret-key}")
	private String secretKey;

	@Value("${minio.bucket-name}")
	private String audioBucketName;

	private final IMinioStorageService minioStorageService;

	@Override
	public void downloadAudio(String videoName, File outputPath) {
		minioStorageService.downloadFile(videoName,outputPath, audioBucketName, secretKey, minioUrl, accessKey);
	}

}
