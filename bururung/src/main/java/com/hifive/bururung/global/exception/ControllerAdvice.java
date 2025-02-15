package com.hifive.bururung.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
	
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.of(e);
    }
    
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        		.body(ErrorResponse.builder()
        				.message("다시 시도해주세요")
        				.code("RUNTIME_ERROR")
        				.status(HttpStatus.BAD_REQUEST)
        				.build());
    }
    
    @ExceptionHandler(value = AuthenticationException.class)
    public void handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication exception occurred: {}", e.getMessage());
    }
}
