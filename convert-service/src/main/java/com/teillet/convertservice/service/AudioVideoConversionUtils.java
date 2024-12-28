package com.teillet.convertservice.service;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;


import java.io.File;
import java.io.IOException;

@Slf4j
public class AudioVideoConversionUtils {

	public static void convertVideoToAudio(File videoFile, File outputAudioFile) {
		try {
			// Configuration de FFmpeg
			FFmpeg ffmpeg = new FFmpeg("ffmpeg/ffmpeg.exe");

			// Construction de la commande FFmpeg
			FFmpegBuilder builder = new FFmpegBuilder()
					.setInput(videoFile.getAbsolutePath())   // Fichier vidéo en entrée
					.addOutput(outputAudioFile.getAbsolutePath()) // Fichier audio en sortie
					.setAudioSampleRate(16_000)             // Fréquence d'échantillonnage 16 kHz
					.setAudioChannels(1)                   // Convertir en mono
					.setAudioCodec("pcm_s16le")            // Codec audio PCM 16 bits
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
