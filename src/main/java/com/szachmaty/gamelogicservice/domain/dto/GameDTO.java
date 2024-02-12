package com.szachmaty.gamelogicservice.domain.dto;


import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.entity.UserEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class GameDTO {
    private String gameCode;
    private UserDTO whiteUser;
    private UserDTO blackUser;
    private LocalTime whiteTime;
    private LocalTime blackTime;
    private GameStatus gameStatus;
    private List<String> boardStateList;
    private List<MoveEntity> moveList;
}
