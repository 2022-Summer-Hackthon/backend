package com.dgsw.hackathon.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor @Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
}
