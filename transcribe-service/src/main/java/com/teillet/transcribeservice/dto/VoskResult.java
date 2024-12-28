package com.teillet.transcribeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoskResult {

	// Getters and Setters
	@JsonProperty("result")
	private List<WordResult> results;

	@JsonProperty("text")
	private String text;

}
