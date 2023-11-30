package com.szachmaty.gamelogicservice.domain.dto;

import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.entity.UserEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GameStateDTO  extends GameEntity {

    private long gameId;
    @ToString.Exclude
    private LocalDateTime whiteTime;
    @ToString.Exclude
    private LocalDateTime blackTime;
    private List<String> boardStateList;

    public GameStateDTO() {
        super();
    }



}
