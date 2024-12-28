package com.teillet.transcribeservice.service;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.teillet.transcribeservice.adapter.VoskWebSocketAdapter;
import com.teillet.transcribeservice.dto.VoskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

@Service
@Slf4j
public class VoskService implements IVoskService {
	public static final int MULTIPLICATEUR = 500;
	private Semaphore semaphore;

	public List<String> transcriptFile(String convertedFilePath) throws WebSocketException, IOException, InterruptedException {
		// Initialiser le sémaphore avec 0 permis
		semaphore = new Semaphore(0);

		List<VoskResult> voskResults = communicateWithVosk(new File(convertedFilePath));
		return voskResults.stream().map(VoskResult::getText).toList();
	}

	private List<VoskResult> communicateWithVosk(File fileIn) throws WebSocketException, IOException, InterruptedException {
		int totalFramesRead = 0;
		List<VoskResult> voskResults = new ArrayList<>();

		WebSocket ws = getWebSocket(voskResults, semaphore);
		log.info("Reading file");
		try(AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn)) {
			AudioFormat format = audioInputStream.getFormat();
			int bytesPerFrame = format.getFrameSize();
			if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
				bytesPerFrame = 1;
			}

			// Informer le serveur Vosk du taux d'échantillonnage du fichier audio
			ws.sendText("{ \"config\" : { \"sample_rate\" : " + (int) format.getSampleRate() + " } }");

			// Définir une taille de tampon arbitraire de 1024 trames.
			int numBytes = 1024 * bytesPerFrame * MULTIPLICATEUR;
			log.info("Num bytes: {}", numBytes);
			byte[] audioBytes = new byte[numBytes];
			int numBytesRead;
			int numFramesRead;

			// Obtenir la taille totale du fichier audio en octets
			long totalBytes = fileIn.length();
			long bytesProcessed = 0;

			// Essayer de lire numBytes octets à partir du fichier.
			while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
				// Calculer le nombre de trames réellement lues.
				numFramesRead = numBytesRead / bytesPerFrame;
				totalFramesRead += numFramesRead;

				bytesProcessed += numBytesRead;
				// Calculer le pourcentage de progression
				int progress = (int) ((bytesProcessed * 100) / totalBytes);
				// Afficher le pourcentage dans les logs
				log.info("Progression : {}%", progress);

				ws.sendBinary(audioBytes);

				// Attendre que le sémaphore soit relâché par le WebSocketAdapter
				semaphore.acquire();
			}
			log.info("Total frames read: {}", totalFramesRead);

			ws.sendText("{\"eof\" : 1}");

			// Attendre la réponse de fin
			semaphore.acquire();
			ws.disconnect();
			log.info("WebSocket disconnected");
		} catch (UnsupportedAudioFileException e) {
			log.error("Unsupported audio file format", e);
		}
		return voskResults;
	}

	public WebSocket getWebSocket(List<VoskResult> voskResults, Semaphore semaphore) throws IOException, WebSocketException {
		WebSocketFactory factory = new WebSocketFactory();
		WebSocket ws = factory.createSocket("ws://localhost:2700");

		// Passez le sémaphore ici
		ws.addListener(new VoskWebSocketAdapter(semaphore, voskResults));
		ws.connect();
		return ws;
	}
}
