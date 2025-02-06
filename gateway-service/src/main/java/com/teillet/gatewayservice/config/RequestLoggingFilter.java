package com.teillet.gatewayservice.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RequestLoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest httpRequest) {
			String method = httpRequest.getMethod();
			String uri = httpRequest.getRequestURI();
			String query = httpRequest.getQueryString();

			log.info("📥 Received request: [{}] {}{}", method, uri, (query != null ? "?" + query : ""));
		}

		chain.doFilter(request, response);
	}
}
