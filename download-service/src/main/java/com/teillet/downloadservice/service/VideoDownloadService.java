package com.teillet.downloadservice.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class VideoDownloadService implements IVideoDownloadService {

	public static final int BUFFER_SIZE = 65536;

	@Override
	public void downloadVideoWithOkHttp(File outputFile, String videoUrl) throws IOException {
		OkHttpClient client = new OkHttpClient();

		log.info("Starting video download. URL: {}, Output file: {}", videoUrl, outputFile.getAbsolutePath());

		// Construire la requête HTTP
		Request httpRequest = new Request.Builder()
				.url(videoUrl)
				.build();

		// Initialiser les variables pour mesurer le temps d'exécution
		long startTime = System.currentTimeMillis();

		try (Response response = client.newCall(httpRequest).execute()) {
			// Vérifier le code de réponse
			if (!response.isSuccessful()) {
				log.error("Failed to download video. URL: {}, HTTP code: {}, Message: {}",
						videoUrl, response.code(), response.message());
				throw new IOException("Failed to download video. HTTP code: " + response.code());
			}
			log.info("Successfully connected to the video URL. HTTP code: {}", response.code());

			// Lire le flux de données et écrire dans le fichier
			if (response.body() == null) {
				throw new IOException("Response body is null");
			}
			try (InputStream inputStream = response.body().byteStream();
			     FileOutputStream outputStream = new FileOutputStream(outputFile)) {
				log.debug("Starting file write operation. Buffer size: 64 KB");

				byte[] buffer = new byte[BUFFER_SIZE]; // 64 Ko
				int bytesRead;
				long totalBytes = 0;

				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
					totalBytes += bytesRead;

					// Log occasionnel pour indiquer la progression
					if (totalBytes % (10 * 1024 * 1024) == 0) { // Tous les 10 Mo
						log.debug("Downloaded {} MB so far for file: {}", totalBytes / (1024 * 1024), outputFile.getName());
					}
				}

				log.info("File write completed. Total size: {} MB", totalBytes / (1024 * 1024));
			}
		} catch (IOException e) {
			log.error("Error occurred while downloading video. URL: {}, Error: {}", videoUrl, e.getMessage(), e);
			throw e;
		}

		// Calculer la durée totale
		long endTime = System.currentTimeMillis();
		log.info("Video download completed. URL: {}, Output file: {}, Time taken: {} ms",
				videoUrl, outputFile.getAbsolutePath(), (endTime - startTime));

	}

}
