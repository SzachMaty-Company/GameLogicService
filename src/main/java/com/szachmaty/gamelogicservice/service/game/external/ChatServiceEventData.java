package com.szachmaty.gamelogicservice.service.game.external;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChatServiceEventData extends ApplicationEvent {
    private final GameDTO gameDTO;

    public ChatServiceEventData(Object source, GameDTO gameDTO) {
        super(source);
        this.gameDTO = gameDTO;
    }
}
