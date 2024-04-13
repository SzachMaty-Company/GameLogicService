package com.szachmaty.gamelogicservice.service.external;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AIMessageEventData extends ApplicationEvent {
    private final String gameCode;
    private final String board;

    public AIMessageEventData(Object source, String gameCode, String board) {
        super(source);
        this.gameCode = gameCode;
        this.board = board;
    }
}
