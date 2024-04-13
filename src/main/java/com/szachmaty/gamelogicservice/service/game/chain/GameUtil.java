package com.szachmaty.gamelogicservice.service.game.chain;

import com.szachmaty.gamelogicservice.data.entity.GameStatus;

public class GameUtil {
    public static boolean finishDeterminator(GameStatus gameStatus) {
        return gameStatus == GameStatus.WHITE_WINNER ||
                gameStatus == GameStatus.BLACK_WINNER ||
                gameStatus == GameStatus.DRAW;
    }
}
