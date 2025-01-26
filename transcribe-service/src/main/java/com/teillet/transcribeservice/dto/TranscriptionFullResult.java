package com.teillet.transcribeservice.dto;

import lombok.Data;

@Data
public class TranscriptionFullResult {
	private String status;
	private String url;
	private String file_name;
	private int file_size;
	private String data; // Le contenu JSON brut de "data"
}
