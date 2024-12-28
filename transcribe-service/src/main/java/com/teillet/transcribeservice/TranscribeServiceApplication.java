package com.teillet.transcribeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(scanBasePackages = "com.teillet")
@EnableRedisRepositories(basePackages = "com.teillet.shared.repository") // Chemin du package contenant ton repository
public class TranscribeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscribeServiceApplication.class, args);
	}

}
