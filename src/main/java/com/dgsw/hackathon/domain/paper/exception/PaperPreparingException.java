package com.dgsw.hackathon.domain.paper.exception;

import com.dgsw.hackathon.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaperPreparingException extends BusinessException {
    public PaperPreparingException() {
        super(HttpStatus.PROCESSING, "server is analyzing websites");
    }
}
