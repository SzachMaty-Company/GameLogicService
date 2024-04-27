package com.szachmaty.gamelogicservice.service.gameinit;


public class GameInitException extends RuntimeException {
    public GameInitException(String message) {
        super(message);
    }
    public GameInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
