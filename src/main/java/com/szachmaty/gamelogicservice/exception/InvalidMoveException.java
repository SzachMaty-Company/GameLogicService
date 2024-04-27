package com.szachmaty.gamelogicservice.exception;

import lombok.Getter;

@Getter
public class InvalidMoveException extends RuntimeException {

    private String move;
    private String board;
    private Long time;
    private String gameCode;

    public InvalidMoveException(String errorDescription, String move, String board, Long time, String gameCode) {
        super(errorDescription);
        this.move = move;
        this.board = board;
        this.time = time;
        this.gameCode = gameCode;
    }
    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidMoveException(String message) {
        super(message);
    }
}
