package com.teillet.shared.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.lang.reflect.Method;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService<T> implements IKafkaProducerService<T> {
	private final KafkaTemplate<String, T> kafkaTemplate;

	@Override
	public void sendMessage(String topic, T request) {
		try {
			// Utilisation de la réflexion pour appeler la méthode getRequestId()
			Method getRequestIdMethod = request.getClass().getMethod("getRequestId");
			CharSequence requestId = (CharSequence) getRequestIdMethod.invoke(request);

			// Envoyer le message avec la clé générée par getRequestId()
			log.info("Envoi du message à Kafka : {}", request);
			kafkaTemplate.send(topic, requestId.toString(), request);
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de l'envoi du message avec la clé générée par getRequestId()", e);
		}
	}
}
