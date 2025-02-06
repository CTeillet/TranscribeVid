package com.teillet.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${cors.allowed.origins}")
	private String[] allowedOrigins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns(allowedOrigins) // Charge dynamiquement les origines autorisées
				.allowedMethods("POST", "OPTIONS") // Autorise seulement POST et OPTIONS
				.allowedHeaders("*"); // Autorise tous les en-têtes
	}
}
