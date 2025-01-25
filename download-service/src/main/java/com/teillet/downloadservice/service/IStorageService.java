package com.teillet.downloadservice.service;

import java.io.File;

public interface IStorageService {
	/**
	 * Upload a video file to minio
	 *
	 * @param file the file to upload
	 * @throws Exception if an error occurs
	 */
	void uploadVideo(File file) throws Exception;
}
