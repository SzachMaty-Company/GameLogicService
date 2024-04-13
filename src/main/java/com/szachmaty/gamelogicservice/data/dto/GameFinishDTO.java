package com.szachmaty.gamelogicservice.data.dto;

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
    private String gameMode;
    private String gameDuration;
    private String gameStartTime;
    private String gameStatus;
    private List<String> fenList;
    private List<String> moveList;
}
