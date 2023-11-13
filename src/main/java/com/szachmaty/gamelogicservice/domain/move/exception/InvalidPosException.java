package com.szachmaty.gamelogicservice.domain.move.exception;

public class InvalidPosException extends RuntimeException {
    public InvalidPosException(String message) {
        super(message);
    }
}
