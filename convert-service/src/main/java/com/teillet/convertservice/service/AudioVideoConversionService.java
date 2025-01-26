package com.teillet.convertservice.service;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class AudioVideoConversionService implements IAudioVideoConversionService {

	@Value("${convert-service.ffmpeg-path}")
	private String ffmpegPath;

	@Override
	public void convertVideoToAudio(File videoFile, File outputAudioFile) {
		try {
			// Configuration de FFmpeg
			FFmpeg ffmpeg = new FFmpeg(ffmpegPath);

			// Construction de la commande FFmpeg
			FFmpegBuilder builder = new FFmpegBuilder()
					.setInput(videoFile.getAbsolutePath())         // Fichier vidéo en entrée
					.addOutput(outputAudioFile.getAbsolutePath())  // Fichier audio en sortie
					.setAudioCodec("libmp3lame")                  // Codec MP3
					.setAudioBitRate(16_000)                      // Débit binaire ultra-réduit à 16 kbps
					.setAudioSampleRate(8_000)                    // Échantillonnage réduit à 8 kHz
					.setAudioChannels(1)                          // Convertir en mono
					.addExtraArgs("-q:a", "9")                    // VBR avec qualité minimale
					.addExtraArgs("-map_metadata", "-1")          // Supprimer toutes les métadonnées
					.setFormat("mp3")                             // Format de conteneur MP3
					.done();

			// Exécution de la commande
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
			executor.createJob(builder).run();

			System.out.println("Conversion réussie : " + outputAudioFile.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de l'exécution de FFmpeg : " + e.getMessage(), e);
		}
	}

}
