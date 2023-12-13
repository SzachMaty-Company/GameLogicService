package com.szachmaty.gamelogicservice.domain.repository.exception;


public class GameEntityDTOConversionException extends GameEntityException {
    public GameEntityDTOConversionException(String message) {
        super(message);
    }

    public GameEntityDTOConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
