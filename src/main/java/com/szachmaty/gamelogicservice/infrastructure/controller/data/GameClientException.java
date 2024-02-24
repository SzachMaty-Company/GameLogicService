package com.szachmaty.gamelogicservice.infrastructure.controller.data;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GameClientException extends RuntimeException {
    private final HttpStatus httpStatus;
    public GameClientException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}