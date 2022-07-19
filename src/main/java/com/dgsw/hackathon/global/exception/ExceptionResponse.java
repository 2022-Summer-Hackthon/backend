package com.dgsw.hackathon.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class ExceptionResponse {
    private final int code;
    private final String message;
}
