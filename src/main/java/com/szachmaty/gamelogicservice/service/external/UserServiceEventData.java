package com.szachmaty.gamelogicservice.service.external;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserServiceEventData extends ApplicationEvent { //refactor - UserServiceEventData
    private final GameDTO gameDTO;

    public UserServiceEventData(Object source, GameDTO gameDTO) {
        super(source);
        this.gameDTO = gameDTO;
    }
}
