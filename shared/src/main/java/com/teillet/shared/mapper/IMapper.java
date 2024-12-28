package com.teillet.shared.mapper;

import com.teillet.shared.avro.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IMapper {

	IMapper INSTANCE = Mappers.getMapper(IMapper.class);

	//Mapping d'une DownloadRequest à une ConversionRequest
	@Mapping(target = "videoFile", ignore = true)
	ConversionRequest toConversionRequest(DownloadRequest request);

	@Mapping(target = "audioFile", ignore = true)
	TranscriptionRequest toTranscriptionRequest(ConversionRequest request);

	@Mapping(target = "transcription", ignore = true)
	CorrectionRequest toCorrectionRequest(TranscriptionRequest request);

	@Mapping(target = "correctedTranscriptionFile", ignore = true)
	MailRequest toMailRequest(CorrectionRequest request);
}
