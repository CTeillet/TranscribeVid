package com.teillet.gatewayservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApiSecretConfig {
	@Value("${API_SECRET}")
	private String apiSecret;

}
