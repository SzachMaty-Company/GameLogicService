package com.szachmaty.gamelogicservice.domain.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class GameStateDTO {

    private long gameId;
    private List<String> boardStateList;

    public GameStateDTO(long gameId, List<String> boardStateList) {
        this.gameId = gameId;
        this.boardStateList = boardStateList;
    }
}
