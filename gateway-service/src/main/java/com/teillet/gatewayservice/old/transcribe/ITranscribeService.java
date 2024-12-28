package com.teillet.gatewayservice.old.transcribe;

import com.neovisionaries.ws.client.WebSocketException;
import com.teillet.gatewayservice.old.recasepunct.RecasePunctException;
import com.teillet.gatewayservice.old.recasepunct.dto.RecasePunctResult;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public interface ITranscribeService {
	RecasePunctResult transcribe(String filePath) throws UnsupportedAudioFileException, IOException, WebSocketException, InterruptedException, RecasePunctException;
}
