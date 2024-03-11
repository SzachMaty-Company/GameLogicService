package com.szachmaty.gamelogicservice.infrastructure.controller.data;

public record GameFinishDTO(boolean isWhiteWinner, boolean isBlackWinner, boolean isDraw, boolean isFinish) {}
