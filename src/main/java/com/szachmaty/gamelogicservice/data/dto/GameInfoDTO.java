package com.szachmaty.gamelogicservice.data.dto;

import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameInfoDTO {
    private String fen;
    private Long whiteTime;
    private Long blackTime;
    private String sideToMove;
    private GameStatus gameStatus;
}
