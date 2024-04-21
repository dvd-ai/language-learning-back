package com.example.languagelearning.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApplicationException.class })
    public ResponseEntity<ErrorDto> onApplicationException(ApplicationException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler(value = {ClientException.class })
    public ResponseEntity<ErrorDto> onClientException(ClientException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
