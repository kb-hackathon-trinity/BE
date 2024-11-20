package org.trinity.be.common.dto;

import org.trinity.be.common.code.ErrorCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ExceptionResDto {

    private String responseCode;
    private String responseMessage;

    public static ResponseEntity<ExceptionResDto> exceptionResponse(final ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ExceptionResDto.builder()
                        .responseCode(errorCode.name())
                        .responseMessage(errorCode.getMessage())
                        .build());
    }
}
