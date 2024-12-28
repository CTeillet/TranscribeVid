package com.teillet.shared.service;

import com.teillet.shared.avro.DownloadRequest;

public interface IKafkaProducerService<T> {
	void sendMessage(String topic, T request);
}
