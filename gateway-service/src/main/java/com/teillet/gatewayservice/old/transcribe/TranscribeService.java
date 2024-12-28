package com.teillet.gatewayservice.old.transcribe;

import com.neovisionaries.ws.client.WebSocketException;
import com.teillet.gatewayservice.old.recasepunct.IRecasePunctService;
import com.teillet.gatewayservice.old.recasepunct.RecasePunctException;
import com.teillet.gatewayservice.old.recasepunct.dto.RecasePunctResult;
import com.teillet.gatewayservice.old.vosk.IVoskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranscribeService implements ITranscribeService {
	private final IVoskService voskService;
	private final IRecasePunctService recasePunctService;
//	private final IConversionService conversionService;

	@Override
	public RecasePunctResult transcribe(String filePath) throws UnsupportedAudioFileException, IOException, WebSocketException, InterruptedException, RecasePunctException {
//		String convertedFilePath = conversionService.convertAudioIfNeeded(filePath);

//		List<String> resultBeforePunctuate = voskService.transcriptFile(convertedFilePath);

//		return recasePunctService.callRecasePuncApi(String.join(" ", resultBeforePunctuate));
		return null;
	}
}
