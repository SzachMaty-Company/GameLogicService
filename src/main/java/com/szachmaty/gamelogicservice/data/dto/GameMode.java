package com.szachmaty.gamelogicservice.data.dto;

import lombok.Getter;

@Getter
public enum GameMode {
    FRIEND("FRIEND"),
    AI("AI");

    private String mode;

    GameMode(String mode) {
        this.mode = mode;
    }

    public static boolean isAIMode(GameMode mode) {
        return AI.equals(mode);
    }
}
