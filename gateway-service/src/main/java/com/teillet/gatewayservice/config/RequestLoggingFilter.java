package com.teillet.gatewayservice.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter("/*")
public class RequestLoggingFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, jakarta.servlet.ServletException {
		if (request instanceof HttpServletRequest httpServletRequest) {
			String method = httpServletRequest.getMethod();
			String uri = httpServletRequest.getRequestURI();
			String query = httpServletRequest.getQueryString();

			logger.info("Received request: [{}] {}{}", method, uri, query != null ? "?" + query : "");
		}

		chain.doFilter(request, response);
	}
}
