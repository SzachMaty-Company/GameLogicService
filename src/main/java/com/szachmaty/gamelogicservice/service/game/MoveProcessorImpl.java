package com.szachmaty.gamelogicservice.service.game;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.exception.InvalidMoveException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


@Service
public class MoveProcessorImpl implements MoveProcessor {

    private final static int MOVE_LEN = 4;
    private final Board board;
    private final static CharSequence PROMOTIONS = "QRBN";
    private final static String MOVE_PROCESSING_ERROR = "An error occured during move processing!";
    private final static String INVALID_PIECE_PROMOTION = "Invalid piece promotion!";


    public MoveProcessorImpl() {
        board = new Board();
    }

    @Override
    public boolean doMove(GameProcessContext gameProcessContext) {
        String currMove = gameProcessContext.getMove();
        Side side = gameProcessContext.getSide();
        String currBoardState = gameProcessContext.getCurrBoardState();

        Move move = prepareMove(currMove, side);
        board.loadFromFen(currBoardState);

        boolean isValid = false;
        if(board.legalMoves().contains(move)) {
            try {
                isValid = board.doMove(move, true);
            } catch(Exception e) {
                throw new InvalidMoveException(MOVE_PROCESSING_ERROR);
            }
        }
        return isValid;
    }

    public LinkedList<Long> getHistory() {
        return board.getHistory();
    }

    public String getBoardState() {
        return board.getFen();
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
}
