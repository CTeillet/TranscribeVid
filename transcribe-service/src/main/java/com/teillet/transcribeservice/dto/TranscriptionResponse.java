package com.teillet.transcribeservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class TranscriptionResponse {
	@JsonAlias("batch_id")
	private String batchId;
}
