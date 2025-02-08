package com.teillet.emailservice.service;

import org.springframework.messaging.MessagingException;

import java.io.File;

public interface IEmailService {
	/**
	 * Envoie un e-mail avec une pièce jointe.
	 *
	 * @param to      L'adresse e-mail du destinataire.
	 * @param subject Le sujet de l'e-mail.
	 * @param text    Le contenu du message.
	 * @param file    Le fichier à attacher à l'e-mail.
	 *                <p>
	 *                Cette méthode crée un message MIME avec le sujet et le texte spécifiés,
	 *                ajoute une pièce jointe si le fichier existe, puis envoie l'e-mail
	 *                en utilisant le service de messagerie configuré.
	 *                <p>
	 *                En cas d'erreur lors de l'envoi, une exception {@link MessagingException}
	 *                est capturée et un message d'erreur est journalisé.
	 */
	void sendEmailWithAttachment(String to, String subject, String text, File file);
}
