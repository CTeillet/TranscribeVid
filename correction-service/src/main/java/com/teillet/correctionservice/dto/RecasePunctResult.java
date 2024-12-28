package com.teillet.correctionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecasePunctResult {

	@JsonProperty("formatted_text")
	private String formattedText;
}
