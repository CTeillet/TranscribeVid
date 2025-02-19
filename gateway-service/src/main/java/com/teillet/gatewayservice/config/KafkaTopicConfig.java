package com.teillet.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_DOWNLOAD_VIDEO_TOPIC;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

	@Value("${kafka.topic.partitions}")
	private int partitions;

	@Value("${kafka.topic.replicas}")
	private int replicas;

	@Bean
	public NewTopic createTopic() {
		return TopicBuilder.name(KAFKA_DOWNLOAD_VIDEO_TOPIC)
				.partitions(partitions)
				.replicas(replicas)
				.build();
	}

}
