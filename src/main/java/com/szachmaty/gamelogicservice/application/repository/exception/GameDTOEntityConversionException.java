package com.szachmaty.gamelogicservice.application.repository.exception;

public class GameDTOEntityConversionException extends GameEntityException {
    public GameDTOEntityConversionException(String message) {
        super(message);
    }

    public GameDTOEntityConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
