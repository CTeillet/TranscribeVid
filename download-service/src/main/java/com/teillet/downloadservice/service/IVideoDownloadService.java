package com.teillet.downloadservice.service;

import java.io.File;
import java.io.IOException;

public interface IVideoDownloadService {
	void downloadVideoWithOkHttp(File outputFile, String videoUrl1) throws IOException;
}
