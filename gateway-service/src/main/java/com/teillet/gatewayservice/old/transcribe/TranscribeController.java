package com.teillet.gatewayservice.old.transcribe;

import com.teillet.gatewayservice.old.recasepunct.dto.RecasePunctResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class TranscribeController {
	private final ITranscribeService transcribeService;

	@GetMapping("/transcribe")
	public RecasePunctResult transcribe() throws Exception {
		String filePath = "C:\\Users\\teill\\IdeaProjects\\TranscribeVid\\src\\main\\ressources\\audioFiles\\Contes_d_ailleurs__Katika.wav";
		return transcribeService.transcribe(filePath);
	}

}
