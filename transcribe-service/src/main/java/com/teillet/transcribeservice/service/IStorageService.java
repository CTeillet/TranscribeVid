package com.teillet.transcribeservice.service;

import java.io.File;

public interface IStorageService {
	void downloadAudio(String audioFile, File outputPath);

	/**
	 * Télécharge un fichier de transcription corrigée vers un bucket MinIO.
	 *
	 * @param file Le fichier contenant la transcription corrigée à uploader.
	 * @throws Exception Si une erreur survient lors du téléchargement du fichier.
	 *                   <p>
	 *                   Cette méthode effectue les actions suivantes :
	 *                   <ul>
	 *                       <li>Log l'opération d'upload avec le nom du bucket.</li>
	 *                       <li>Utilise le service {@code minioStorageService} pour téléverser le fichier dans MinIO.</li>
	 *                       <li>Log la réussite de l'opération avec le nom du bucket et du fichier.</li>
	 *                   </ul>
	 *                   <p>
	 *                   En cas d'échec du téléchargement, une exception est levée.
	 */
	void uploadCorrectedTranscription(File file) throws Exception;
}
