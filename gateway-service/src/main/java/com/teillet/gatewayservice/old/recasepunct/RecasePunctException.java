package com.teillet.gatewayservice.old.recasepunct;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class RecasePunctException extends Exception {
	private final HttpStatusCode statusCode;

	public RecasePunctException(HttpStatusCode statusCode) {
		super("Erreur lors de l'appel API : " + statusCode);
		this.statusCode = statusCode;
	}

	public RecasePunctException(HttpStatusCode statusCode, String responseBody, Throwable cause) {
		super("Erreur lors de l'appel API : " + statusCode + " - Réponse : " + responseBody, cause);
		this.statusCode = statusCode;
	}

	public RecasePunctException(Throwable cause) {
		super("Erreur lors de l'appel API", cause);
		this.statusCode = null; // Pas de code d'état associé
	}

}

