package com.szachmaty.gamelogicservice.data.dto;

public record MoveResponseDTO(String move, String fen, Long time, String nextMoveSide, String errorMessage) {
    public MoveResponseDTO(String move, String fen, Long time, String nextMoveSide) {
        this(move, fen, time, nextMoveSide, null);
    }
}
