package com.teillet.shared.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


@Service
@Slf4j
public class MinioStorageService implements IMinioStorageService{

	@Override
	public void uploadFile(File filePath, String bucketName, String secretKey, String minioUrl, String accessKey) throws Exception {
		MinioClient minioClient = MinioClient.builder()
				.endpoint(minioUrl)
				.credentials(accessKey, secretKey)
				.build();

		try (InputStream inputStream = new FileInputStream(filePath)) {
			minioClient.putObject(
					PutObjectArgs.builder()
							.bucket(bucketName)
							.object(filePath.getName())
							.stream(inputStream, -1, 10485760) // 10MB buffer size
							.build()
			);
		}
	}

	@Override
	public void downloadFile(String objectName, File outputPath, String bucketName, String secretKey, String minioUrl, String accessKey) {
		MinioClient minioClient = MinioClient.builder()
				.endpoint(minioUrl)
				.credentials(accessKey, secretKey)
				.build();

		try {
			// Vérifier si l'objet existe
			boolean objectExists = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build()) != null;

			if (!objectExists) {
				throw new RuntimeException("The object '" + objectName + "' does not exist in bucket '" + bucketName + "'.");
			}

			// Télécharger l'objet
			try (InputStream stream = minioClient.getObject(
					GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
			     FileOutputStream outputStream = new FileOutputStream(outputPath)) {

				byte[] buffer = new byte[8192]; // 8 KB buffer
				int bytesRead;
				while ((bytesRead = stream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				log.info("File '{}' successfully downloaded to '{}'", objectName, outputPath);
			}
		} catch (Exception e) {
			log.error("Error downloading file '{}' from bucket '{}'", objectName, bucketName, e);
			throw new RuntimeException("File download failed", e);
		}
	}


}
