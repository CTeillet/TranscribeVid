package com.teillet.gatewayservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Permet toutes les URL
				.allowedOriginPatterns("http://localhost:[*]") // Autorise uniquement votre frontend
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Autorise ces méthodes HTTP
				.allowedHeaders("*") // Autorise tous les en-têtes
				.allowCredentials(true); // Autorise les cookies ou l'authentification
	}
}
