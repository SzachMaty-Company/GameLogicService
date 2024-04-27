package com.szachmaty.gamelogicservice.data.dto;

import com.szachmaty.gamelogicservice.data.entity.GameStatus;

public record MoveResponseDTO(String move, String fen, Long time, String nextMoveSide, GameStatus gameStatus, String errorMessage) {
    public MoveResponseDTO(String move, String fen, Long time, String nextMoveSide, GameStatus gameStatus) {
        this(move, fen, time, nextMoveSide, gameStatus,null);
    }
}
