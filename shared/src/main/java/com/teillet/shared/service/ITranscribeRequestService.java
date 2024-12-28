package com.teillet.shared.service;

import com.teillet.shared.model.LaunchProcessRequest;

public interface ITranscribeRequestService {
	LaunchProcessRequest saveRequest(LaunchProcessRequest request);

	LaunchProcessRequest getRequest(String id);
}
