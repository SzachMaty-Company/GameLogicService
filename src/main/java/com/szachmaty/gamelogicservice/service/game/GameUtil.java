package com.szachmaty.gamelogicservice.service.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;

public class GameUtil {
    public static boolean finishDeterminator(GameStatus gameStatus) {
        return gameStatus == GameStatus.WHITE_WINNER ||
                gameStatus == GameStatus.BLACK_WINNER ||
                gameStatus == GameStatus.DRAW;
    }

    public static MoveResponseDTO contextToMoveRespone(GameProcessContext context) {
        Long time;
        Side nextMove;
        if(context.getSide() == Side.WHITE) {
            time = context.getWhiteTime();
            nextMove = Side.BLACK;
        } else {
            time = context.getBlackTime();
            nextMove = Side.WHITE;
        }
        return new MoveResponseDTO(context.getMove(),
                context.getNextFen(), time, nextMove.toString(), context.getGameStatus());
    }
}
