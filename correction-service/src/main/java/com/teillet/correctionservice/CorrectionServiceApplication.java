package com.teillet.correctionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(scanBasePackages = "com.teillet")
@EnableRedisRepositories(basePackages = "com.teillet.shared.repository") // Chemin du package contenant ton repository
public class CorrectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorrectionServiceApplication.class, args);
	}

}
