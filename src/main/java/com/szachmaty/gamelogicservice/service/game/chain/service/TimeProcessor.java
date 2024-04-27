package com.szachmaty.gamelogicservice.service.game.chain.service;

public interface TimeProcessor {
    Long countTime(boolean isFirstMove, Long prevSystemTime, Long currSystemTime, Long playerTime);
}
