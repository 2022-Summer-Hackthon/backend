package com.dgsw.hackathon.global.handler;

import com.dgsw.hackathon.global.exception.BusinessException;
import com.dgsw.hackathon.global.exception.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException ex) {
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(ex.getStatus().value(), ex.getMessage()),
                ex.getStatus());
    }

}
