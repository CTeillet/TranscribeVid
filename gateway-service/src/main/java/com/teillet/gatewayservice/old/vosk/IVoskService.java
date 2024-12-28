package com.teillet.gatewayservice.old.vosk;

import com.neovisionaries.ws.client.WebSocketException;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

public interface IVoskService {
	List<String> transcriptFile(String convertedFilePath) throws WebSocketException, IOException, UnsupportedAudioFileException, InterruptedException;
}
