package com.szachmaty.gamelogicservice.domain.repository.exception;

public class IncorrectConvertTypeException extends GameEntityDTOConversionException {
    public IncorrectConvertTypeException(String message) {
        super(message);
    }

    public IncorrectConvertTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
