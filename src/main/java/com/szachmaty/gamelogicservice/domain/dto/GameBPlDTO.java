package com.szachmaty.gamelogicservice.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
public class GameBPlDTO extends GameStateDTO {
    private UserDTO bPlDTO;
    private LocalTime blackTime;

    public GameBPlDTO(long gameId, List<String> boardStateList, LocalTime blackTime, UserDTO bPlDTO) {
        super(gameId,boardStateList);
        this.blackTime = blackTime;
        this.bPlDTO = bPlDTO;
    }
    public GameBPlDTO() {
        super();
    }
    @Override
    public String toString() {
        return bPlDTO.getUsername() + " " + getGameId() + " " + getBoardStateList().get(0);
    }
}
