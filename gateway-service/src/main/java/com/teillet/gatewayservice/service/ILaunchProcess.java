package com.teillet.gatewayservice.service;

import com.teillet.shared.model.LaunchProcessRequest;
import com.teillet.gatewayservice.dto.TranscribeRequestDto;

public interface ILaunchProcess {
	LaunchProcessRequest launchProcess(TranscribeRequestDto requestDto);
}
