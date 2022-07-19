package com.dgsw.hackathon.domain.paper.exception;

import com.dgsw.hackathon.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaperNotFoundException extends BusinessException {
    public PaperNotFoundException() {
        super(HttpStatus.NOT_FOUND, "no such paper draft");
    }
}
