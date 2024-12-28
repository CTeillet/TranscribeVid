package com.teillet.emailservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements IEmailService {
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	@Override
	public void sendEmailWithAttachment(String to, String subject, String text, File file) {
		try {
			// Création du message MIME
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);

			// Ajout de la pièce jointe
			if (file.exists()) {
				helper.addAttachment(file.getName(), file);
			}

			// Envoi de l'e-mail
			mailSender.send(message);
			log.info("E-mail envoyé avec succès !");
		} catch (MessagingException e) {
			log.error("Erreur lors de l'envoi de l'e-mail", e);
		}
	}
}
