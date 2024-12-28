package com.teillet.gatewayservice.mapper;

import com.teillet.gatewayservice.dto.TranscribeRequestDto;
import com.teillet.shared.model.LaunchProcessRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TranscribeRequestMapper {

	TranscribeRequestMapper INSTANCE = Mappers.getMapper(TranscribeRequestMapper.class);

	// Mapping du DTO à l'entité avec génération de l'UUID pour requestId
	@Mapping(target = "requestId", ignore = true)
	LaunchProcessRequest toEntity(TranscribeRequestDto dto);

}
