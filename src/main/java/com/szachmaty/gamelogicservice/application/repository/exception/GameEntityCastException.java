package com.szachmaty.gamelogicservice.application.repository.exception;

public class GameEntityCastException extends GameEntityException {
    public GameEntityCastException(String message) {
        super(message);
    }
    public GameEntityCastException(String message, Throwable cause) {
        super(message, cause);
    }
}