package com.teillet.correctionservice.service;

import com.teillet.correctionservice.dto.RecasePunctResult;
import com.teillet.correctionservice.exception.RecasePunctException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecasePunctService implements IRecasePunctService {
	@Value("${recasepunct.api.url}")
	private final String recasePunctApiUrl;

	public RecasePunctResult callRecasePuncApi(String text) throws RecasePunctException {
		HttpEntity<String> httpEntity = getStringHttpEntity(text);
		RestTemplate restTemplate = new RestTemplate();

		try {
			// Envoyer la requête et obtenir la réponse
			ResponseEntity<RecasePunctResult> response = restTemplate.exchange(recasePunctApiUrl, HttpMethod.POST, httpEntity, RecasePunctResult.class);

			// Vérifier si la réponse est valide
			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody();
			} else {
				throw new RecasePunctException(response.getStatusCode());
			}
		} catch (HttpClientErrorException e) {
			// Gérer les erreurs HTTP ici
			throw new RecasePunctException(e.getStatusCode(), e.getResponseBodyAsString(), e);
		} catch (RestClientException e) {
			// Gérer d'autres exceptions liées au client REST
			throw new RecasePunctException(e);
		}
	}

	private static HttpEntity<String> getStringHttpEntity(String text) {
		// Créer les en-têtes de la requête
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Créer le corps de la requête
		String jsonInputString = String.format("{\"text\": \"%s\"}", text);

		// Créer l'entité de la requête avec le corps et les en-têtes
		return new HttpEntity<>(jsonInputString, headers);
	}

}
