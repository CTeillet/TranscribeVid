package com.teillet.gatewayservice.old.recasepunct.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecasePunctResult {

	@JsonProperty("formatted_text")
	private String formattedText;

	@Override
	public String toString() {
		return "RecasePunctResult{" +
				"formattedText='" + formattedText + '\'' +
				'}';
	}
}
