package com.teillet.gatewayservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Permet toutes les URL
				.allowedOriginPatterns("*") // Autorise uniquement votre frontend
				.allowedMethods("POST") // Autorise ces méthodes HTTP
				.allowedHeaders("*") // Autorise tous les en-têtes
				.allowCredentials(true); // Autorise les cookies ou l'authentification
	}
}
