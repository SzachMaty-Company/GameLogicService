package com.szachmaty.gamelogicservice.application.repository.exception;

public class GameEntityNotFoundException extends GameEntityException {
    public GameEntityNotFoundException(String message) {
        super(message);
    }
    public GameEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
