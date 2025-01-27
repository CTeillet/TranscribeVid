package com.teillet.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final ApiTokenFilter apiTokenFilter;
	private final RequestLoggingFilter requestLoggingFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // Désactiver CSRF pour une API REST
				.authorizeHttpRequests(auth -> auth
						.anyRequest().authenticated() // Toutes les requêtes doivent être authentifiées
				)
				.addFilterBefore(apiTokenFilter, UsernamePasswordAuthenticationFilter.class) // Vérifie le token en premier
				.addFilterAfter(requestLoggingFilter, ApiTokenFilter.class); // Log après l'authentification

		return http.build();
	}
}
