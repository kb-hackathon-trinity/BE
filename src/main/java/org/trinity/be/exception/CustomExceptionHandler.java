package org.trinity.be.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.trinity.be.common.dto.ExceptionResDto;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ExceptionResDto> handlerCustomException(CustomException e) {

        log.info(e.getMessage());
        return ExceptionResDto.exceptionResponse(e.getExceptionCode());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String responseCode = exception.getFieldError().getField()
                .replaceAll("[^\\p{Alnum}]+", "_")
                .replaceAll("(\\p{Lower})(\\p{Upper})", "$1_$2")
                .toUpperCase();
        String responseMessage = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();

        responseCode = formatIfInnerDto(responseCode);
        responseCode += formatResponseCode(responseMessage);
        return ResponseEntity.status(status)
                .body(ExceptionResDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build());
    }

    private String formatResponseCode(String responseMessage) {
        String responseCode = "";

        if (responseMessage.contains("필수 입력입니다.") || responseMessage.contains("첨부해 주세요")) {
            responseCode = "_FIELD_REQUIRED"; // @NotBlank, @NotNull
        } else if (responseMessage.contains("~")) {
            responseCode = "_LENGTH_INVALID"; // @Size
        } else if (responseMessage.contains("형식") || responseMessage.contains("조합")) {
            responseCode = "_FORMAT_INVALID"; // @Pattern, @Email - format
        } else if (responseMessage.contains("중 하나여야 됩니다.")) {
            responseCode = "_TYPE_INVALID"; // @Pattern - type
        } else if (responseMessage.contains("까지의 수만")) {
            responseCode = "_RANGE_INVALID"; // @Range
        } else if (responseMessage.contains("0 또는 양수")) {
            responseCode = "_POSITIVE_OR_ZERO_ONLY"; // @PositiveOrZero
        } else if (responseMessage.contains("양수")) {
            responseCode = "_POSITIVE_ONLY"; // @Positive
        } else if (responseMessage.contains("요청")) {
            responseCode = "REQUEST_INVALID";
        }

        return responseCode;
    }

    private String formatIfInnerDto(String responseCode) {
        if (!responseCode.matches(".*\\d+.*"))
            return responseCode;

        for (int i = responseCode.length() - 1; i >= 0; i--) {
            char c = responseCode.charAt(i);
            if (Character.isDigit(c))
                return responseCode.substring(i + 2);
        }

        return responseCode;
    }
}
