package com.teillet.gatewayservice.controller;

import com.teillet.gatewayservice.dto.TranscribeRequestDto;
import com.teillet.gatewayservice.service.ILaunchProcess;
import com.teillet.shared.model.LaunchProcessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transcribe-requests")
@RequiredArgsConstructor
@Slf4j
public class TranscribeRequestController {
	private final ILaunchProcess launchProcess;

	@PostMapping
	public ResponseEntity<LaunchProcessRequest> createTranscribeRequest(@RequestBody TranscribeRequestDto requestDto) {
		log.info("Creating transcribe request: {}", requestDto);
		LaunchProcessRequest savedRequest = launchProcess.launchProcess(requestDto);
		return ResponseEntity.ok(savedRequest);
	}

}
