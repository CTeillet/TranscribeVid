package com.teillet.gatewayservice.old.vosk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoskResult {

	// Getters and Setters
	@JsonProperty("result")
	private List<WordResult> results;

	@JsonProperty("text")
	private String text;

}
