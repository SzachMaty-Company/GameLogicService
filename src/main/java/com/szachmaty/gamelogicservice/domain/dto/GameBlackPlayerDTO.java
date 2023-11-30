package com.szachmaty.gamelogicservice.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
public class GameBlackPlayerDTO extends GameStateDTO {

    private LocalTime blackTime;

    public GameBlackPlayerDTO(long gameId, List<String> boardStateList, LocalTime blackTime) {
        super(gameId,boardStateList);
        this.blackTime = blackTime;
    }
}
