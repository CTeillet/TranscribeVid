package com.teillet.shared.service;


import java.io.File;

public interface IMinioStorageService {
	void uploadFile(File filePath, String bucketName, String secretKey, String minioUrl, String accessKey) throws Exception;
	void downloadFile(String objectName, File outputPath, String bucketName, String secretKey, String minioUrl, String accessKey);
}
