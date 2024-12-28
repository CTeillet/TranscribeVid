package com.teillet.shared.mapper;

import com.teillet.shared.avro.ConversionRequest;
import com.teillet.shared.avro.CorrectionRequest;
import com.teillet.shared.avro.DownloadRequest;
import com.teillet.shared.avro.MailRequest;
import com.teillet.shared.avro.TranscriptionRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-31T01:47:27+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class IMapperImpl implements IMapper {

    @Override
    public ConversionRequest toConversionRequest(DownloadRequest request) {
        if ( request == null ) {
            return null;
        }

        ConversionRequest.Builder conversionRequest = ConversionRequest.newBuilder();

        conversionRequest.setRequestId( request.getRequestId() );
        conversionRequest.setTimestamp( request.getTimestamp() );

        return conversionRequest.build();
    }

    @Override
    public TranscriptionRequest toTranscriptionRequest(ConversionRequest request) {
        if ( request == null ) {
            return null;
        }

        TranscriptionRequest.Builder transcriptionRequest = TranscriptionRequest.newBuilder();

        transcriptionRequest.setRequestId( request.getRequestId() );
        transcriptionRequest.setTimestamp( request.getTimestamp() );

        return transcriptionRequest.build();
    }

    @Override
    public CorrectionRequest toCorrectionRequest(TranscriptionRequest request) {
        if ( request == null ) {
            return null;
        }

        CorrectionRequest.Builder correctionRequest = CorrectionRequest.newBuilder();

        correctionRequest.setRequestId( request.getRequestId() );
        correctionRequest.setTimestamp( request.getTimestamp() );

        return correctionRequest.build();
    }

    @Override
    public MailRequest toMailRequest(CorrectionRequest request) {
        if ( request == null ) {
            return null;
        }

        MailRequest.Builder mailRequest = MailRequest.newBuilder();

        mailRequest.setRequestId( request.getRequestId() );
        mailRequest.setTimestamp( request.getTimestamp() );

        return mailRequest.build();
    }
}
