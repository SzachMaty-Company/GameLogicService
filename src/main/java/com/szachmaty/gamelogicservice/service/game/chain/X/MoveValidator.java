package com.szachmaty.gamelogicservice.service.game.chain.X;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.exception.InvalidMoveException;
import com.szachmaty.gamelogicservice.service.game.chain.GameProcessHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class MoveValidator implements GameProcessHandler {

    private final static int MOVE_LEN = 4;
    private final static String MOVE_PROCESSING_ERROR = "An error occured during move processing!";
    private final static String INVALID_PIECE_PROMOTION = "Invalid piece promotion!";

    @Override
    public boolean process(GameProcessContext context) {
        String currMove = context.getMove();
        Side side = context.getSide();
        String currBoardState = context.getCurrBoardState();
        Board board = new Board();

        Move move = prepareMove(currMove, side);
        board.loadFromFen(currBoardState);

        boolean isMoveValid = false;
        if(board.legalMoves().contains(move)) {
            try {
                isMoveValid = board.doMove(move, true);
            } catch(Exception e) {
                throw new InvalidMoveException(MOVE_PROCESSING_ERROR);
            }
        }

        if(isMoveValid) {
            context.setAfterMoveBoardState(board.getFen());
            context.setGameHistory(board.getHistory());
        } else {
            throw buildInvalidMoveException(context);
        }
        return true;
    }

    private Move prepareMove(String currMove, Side side) {
        if(currMove.length() > MOVE_LEN) {
            String promotion = String.valueOf(currMove.charAt(4));
            if(!StringUtils.containsAnyIgnoreCase(promotion, "Q", "R", "B", "N")) {
                throw new InvalidMoveException(INVALID_PIECE_PROMOTION);
            }
        }
        return new Move(currMove, side);
    }

    private InvalidMoveException buildInvalidMoveException(GameProcessContext context) {
        return new InvalidMoveException(String.format("Move: %s is invalid!", context.getMove()),
                context.getMove(),
                context.getCurrBoardState(),
                context.getSide() == Side.WHITE ? context.getWhiteTime() : context.getBlackTime(),
                context.getGameCode()
        );
    }
}
