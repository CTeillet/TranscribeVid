package com.teillet.convertservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.teillet.shared.utils.KafkaConstant.KAFKA_TRANSCRIPT_AUDIO_TOPIC;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

	@Value("${kafka.topic.partitions}")
	private int partitions;

	@Value("${kafka.topic.replicas}")
	private int replicas;

	@Bean
	/**
	 * Create a new topic with the name KAFKA_TRANSCRIPT_AUDIO_TOPIC
	 */
	public NewTopic createTopic() {
		return TopicBuilder.name(KAFKA_TRANSCRIPT_AUDIO_TOPIC)
				.partitions(partitions)
				.replicas(replicas)
				.build();
	}

}
