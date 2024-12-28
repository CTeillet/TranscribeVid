package com.teillet.emailservice.service;

import java.io.File;

public interface IEmailService {
	void sendEmailWithAttachment(String to, String subject, String text, File file);
}
