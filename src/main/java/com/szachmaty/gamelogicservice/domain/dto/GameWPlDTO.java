package com.szachmaty.gamelogicservice.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class GameWPlDTO extends GameStateDTO {

    private UserDTO wPlDTO;
    private LocalTime whiteTime;

    public GameWPlDTO(long gameId, List<String> boardStateList, LocalTime whiteTime, UserDTO wPlDTO) {
        super(gameId, boardStateList);
        this.whiteTime = whiteTime;
        this.wPlDTO = wPlDTO;
    }
    public GameWPlDTO() {
        super();
    }
    @Override
    public String toString() {
        return wPlDTO.getUsername() + " " + getGameId() + " " + getBoardStateList().get(0);
    }
}
