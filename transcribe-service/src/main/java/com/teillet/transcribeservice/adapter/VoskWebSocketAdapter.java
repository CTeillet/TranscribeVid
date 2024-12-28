package com.teillet.transcribeservice.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.teillet.transcribeservice.dto.VoskResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

@Slf4j
public class VoskWebSocketAdapter extends WebSocketAdapter {
	private final List<VoskResult> voskResults;
	private final Semaphore semaphore;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public VoskWebSocketAdapter(Semaphore semaphore, List<VoskResult> voskResults) {
		this.semaphore = semaphore;
		this.voskResults = voskResults;
	}

	@Override
	public void onTextMessage(WebSocket websocket, String message) {
		try {
			JsonNode jsonNode = objectMapper.readTree(message);

			if (jsonNode.has("result")) {
				// Message complet
				VoskResult voskResult = objectMapper.readValue(message, VoskResult.class);
				// Traiter le résultat complet
				voskResults.add(voskResult);
			}
		} catch (IOException e) {
			log.error("Erreur lors de la lecture des résultats de vosk", e);
		}

		// Relâcher un permis sur le sémaphore pour indiquer que la réponse a été reçue
		semaphore.release();
	}

}
