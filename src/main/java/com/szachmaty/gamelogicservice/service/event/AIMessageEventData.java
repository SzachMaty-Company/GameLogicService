package com.szachmaty.gamelogicservice.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AIMessageEventData extends ApplicationEvent {
    private final String gameCode;
    private final String board;
    private final boolean isWhiteAndFirstCall;

    public AIMessageEventData(Object source, String gameCode, String board, boolean isWhiteAndFirstCall) {
        super(source);
        this.gameCode = gameCode;
        this.board = board;
        this.isWhiteAndFirstCall = isWhiteAndFirstCall;
    }
}
