package com.example.issuetracker.exception;

import com.example.issuetracker.domain.ResponseMessageEnum;

public class BadRequestException extends RuntimeException {

    private final ResponseMessageEnum responseMessage;

    public BadRequestException(ResponseMessageEnum responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public ResponseMessageEnum getResponseMessage() {
        return responseMessage;
    }
}
