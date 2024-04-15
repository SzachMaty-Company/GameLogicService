package com.szachmaty.gamelogicservice.service.event;

import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChatServiceEventData extends ApplicationEvent {
    private final GameInitNotification gameInitNotification;

    public ChatServiceEventData(Object source, GameInitNotification gameInitNotification) {
        super(source);
        this.gameInitNotification = gameInitNotification;
    }
}
