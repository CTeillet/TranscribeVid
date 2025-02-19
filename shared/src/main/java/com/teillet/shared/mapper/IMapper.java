package com.teillet.shared.mapper;

import com.teillet.shared.avro.ConversionRequest;
import com.teillet.shared.avro.DownloadRequest;
import com.teillet.shared.avro.MailRequest;
import com.teillet.shared.avro.TranscriptionRequest;
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

	@Mapping(target = "correctedTranscriptionFile", ignore = true)
	MailRequest toMailRequest(TranscriptionRequest request);
}
