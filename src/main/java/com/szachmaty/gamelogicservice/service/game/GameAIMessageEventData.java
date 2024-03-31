package com.szachmaty.gamelogicservice.service.game;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GameAIMessageEventData extends ApplicationEvent {
    private final String gameCode;
    private final String board;

    public GameAIMessageEventData(Object source, String gameCode, String board) {
        super(source);
        this.gameCode = gameCode;
        this.board = board;
    }
}
