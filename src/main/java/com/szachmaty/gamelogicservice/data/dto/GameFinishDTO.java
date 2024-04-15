package com.szachmaty.gamelogicservice.data.dto;

import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class GameFinishDTO implements Serializable {
    private String gameCode;
    private String whiteUserId;
    private String blackUserId;
    private GameMode gameMode;
    private String gameTime;
    private String gameStartTime;
    private GameStatus gameStatus;
    private List<String> fenList;
    private List<String> moveList;
}
