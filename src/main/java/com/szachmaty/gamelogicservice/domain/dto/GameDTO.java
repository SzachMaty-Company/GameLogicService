package com.szachmaty.gamelogicservice.domain.dto;


import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class GameDTO {
    private String gameCode;
    private UserDTO whiteUser;
    private UserDTO blackUser;
    private Long whiteTime;
    private Long blackTime;
    private Long prevMoveTime;
    private GameStatus gameStatus;
    private List<String> boardStateList;
    private LinkedList<Long> gameHistory;
    private List<MoveEntity> moveList;
}
