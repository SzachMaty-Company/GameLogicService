package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class MoveProcessorImpl implements MoveProcessor {

    private final static int MOVE_LEN = 4;

    @Override
    public String doMove(String currMove, String currBoardState, Side side) {

        Move move = prepareMove(currMove, side);
        Board board = new Board();
        board.loadFromFen(currBoardState);

        boolean isValid = board.doMove(move, true);

        if(isValid) {
            return board.getFen();
        } else {
            throw new InvalidMoveException("Move: " + currMove + " is invalid!");
        }
    }

    private Move prepareMove(String currMove, Side side) {
        if(currMove.length() > MOVE_LEN) {
            String promotion = String.valueOf(currMove.charAt(4));
            if(!StringUtils.containsAnyIgnoreCase(promotion, "Q", "R", "B", "K")) {
                throw new InvalidMoveException("Invalid piece promotion!");
            }
        }
        return new Move(currMove, side);
    }
}
