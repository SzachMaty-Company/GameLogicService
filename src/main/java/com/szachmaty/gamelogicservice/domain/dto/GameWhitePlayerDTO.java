package com.szachmaty.gamelogicservice.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class GameWhitePlayerDTO extends GameStateDTO {
    private LocalTime whiteTime;

    public GameWhitePlayerDTO(long gameId, List<String> boardStateList, LocalTime whiteTime) {
        super(gameId, boardStateList);
        this.whiteTime = whiteTime;
    }
}
