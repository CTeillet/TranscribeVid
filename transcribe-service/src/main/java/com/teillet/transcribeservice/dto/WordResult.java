package com.teillet.transcribeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WordResult {

	// Getters and Setters
	@JsonProperty("conf")
	private double confidence;

	@JsonProperty("end")
	private double endTime;

	@JsonProperty("start")
	private double startTime;

	@JsonProperty("word")
	private String word;

}
