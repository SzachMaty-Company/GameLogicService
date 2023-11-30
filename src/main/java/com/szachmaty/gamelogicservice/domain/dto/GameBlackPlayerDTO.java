package com.szachmaty.gamelogicservice.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class GameBlackPlayerDTO extends GameStateDTO {

    private LocalDateTime blackTime; //TO BE CHANGED

    public GameBlackPlayerDTO(long gameId, List<String> boardStateList, LocalDateTime blackTime) {
        super(gameId,boardStateList);
        this.blackTime = blackTime;
    }
}
