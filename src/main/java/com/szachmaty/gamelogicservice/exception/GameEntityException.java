package com.szachmaty.gamelogicservice.exception;

public class GameEntityException extends RuntimeException {
    public GameEntityException(String message) {
        super(message);
    }
    public GameEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
