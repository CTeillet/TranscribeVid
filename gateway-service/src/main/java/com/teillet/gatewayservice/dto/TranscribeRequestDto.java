package com.teillet.gatewayservice.dto;

import lombok.Data;

@Data
public class TranscribeRequestDto {
	private String email;
	private String videoUrl;
}
