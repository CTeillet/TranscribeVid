package com.teillet.gatewayservice.old.vosk;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.teillet.gatewayservice.old.vosk.dto.VoskResult;
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
	private Semaphore semaphore;

	public List<String> transcriptFile(String convertedFilePath) throws WebSocketException, IOException, UnsupportedAudioFileException, InterruptedException {
		// Initialiser le sémaphore avec 0 permis
		semaphore = new Semaphore(0);

		List<VoskResult> voskResults = communicateWithVosk(new File(convertedFilePath));
		return voskResults.stream().map(VoskResult::getText).toList();
	}

	private List<VoskResult> communicateWithVosk(File fileIn) throws WebSocketException, IOException, UnsupportedAudioFileException, InterruptedException {
		int totalFramesRead = 0;
		List<VoskResult> voskResults = new ArrayList<>();

		WebSocket ws = getWebSocket(voskResults);
		log.info("Reading file");
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
		AudioFormat format = audioInputStream.getFormat();
		int bytesPerFrame = format.getFrameSize();
		if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
			bytesPerFrame = 1;
		}

		// Informer le serveur Vosk du taux d'échantillonnage du fichier audio
		ws.sendText("{ \"config\" : { \"sample_rate\" : " + (int) format.getSampleRate() + " } }");

		// Définir une taille de tampon arbitraire de 1024 trames.
		int numBytes = 1024 * bytesPerFrame;
		log.info("Num bytes: {}", numBytes);
		byte[] audioBytes = new byte[numBytes];
		int numBytesRead;
		int numFramesRead;
		// Essayer de lire numBytes octets à partir du fichier.
		while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
			// Calculer le nombre de trames réellement lues.
			numFramesRead = numBytesRead / bytesPerFrame;
			totalFramesRead += numFramesRead;

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

		return voskResults;
	}



	private WebSocket getWebSocket(List<VoskResult> voskResults) throws IOException, WebSocketException {
		WebSocketFactory factory = new WebSocketFactory();
		WebSocket ws = factory.createSocket("ws://localhost:2700");

		// Passez le sémaphore ici
		ws.addListener(new VoskWebSocketAdapter(semaphore, voskResults));
		ws.connect();
		return ws;
	}
}
