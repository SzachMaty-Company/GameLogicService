package com.szachmaty.gamelogicservice.infrastructure.controller.data;

public record MoveResponseDTO(String move, String fen, Long time, String errorMessage) {
    public MoveResponseDTO(String move, String fen, Long time) {
        this(move, fen, time, null);
    }
}
