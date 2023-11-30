package com.szachmaty.gamelogicservice.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GameWhitePlayerDTO extends GameStateDTO {
    private LocalDateTime whiteTime;

    public GameWhitePlayerDTO(long gameId, List<String> boardStateList, LocalDateTime whiteTime) {
        super(gameId, boardStateList);
        this.whiteTime = whiteTime;
    }
}
