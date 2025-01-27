package com.teillet.gatewayservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiTokenFilter extends OncePerRequestFilter {

	private final ApiSecretConfig apiSecretConfig;

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
			throws ServletException, IOException {

		// Récupérer le token
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.info("🔍 Checking Authorization header: {}", token);

		// Vérifier si le token est valide
		if (token == null || !token.equals("Bearer " + apiSecretConfig.getApiSecret())) {
			log.warn("❌ Unauthorized request: [{}] {} from {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		log.info("✅ Authorized request from {}", request.getRemoteAddr());

		// Ajouter l'utilisateur authentifié dans le SecurityContext
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken("api-client", null, Collections.emptyList());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Continuer la chaîne de filtres
		filterChain.doFilter(request, response);
	}
}
