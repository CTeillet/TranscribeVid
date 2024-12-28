package com.teillet.convertservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(scanBasePackages = "com.teillet")
@EnableRedisRepositories(basePackages = "com.teillet.shared.repository")
public class ConvertServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConvertServiceApplication.class, args);
	}

}
