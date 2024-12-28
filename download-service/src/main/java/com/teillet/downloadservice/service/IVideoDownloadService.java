package com.teillet.downloadservice.service;

import com.teillet.shared.model.LaunchProcessRequest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface IVideoDownloadService {
	File downloadVideo(LaunchProcessRequest launchProcessRequest, File outputFile) throws IOException, URISyntaxException;

	File downloadVideoWithOkHttp(File outputFile, String videoUrl1) throws IOException;
}
