package com.teillet.shared.service;

import com.teillet.shared.model.LaunchProcessRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.teillet.shared.repository.TranscribeRequestRepository;

@Service
@RequiredArgsConstructor
public class TranscribeRequestService implements ITranscribeRequestService {
	private final TranscribeRequestRepository transcribeRequestRepository;

	@Override
	public LaunchProcessRequest saveRequest(LaunchProcessRequest request) {
		return transcribeRequestRepository.save(request);
	}

	@Override
	public LaunchProcessRequest getRequest(String id) {
		return transcribeRequestRepository.findById(id).orElse(null);
	}
}
