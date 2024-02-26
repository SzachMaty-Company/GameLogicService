package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


@Service
public class MoveProcessorImpl implements MoveProcessor {

    private final static int MOVE_LEN = 4;
    private final Board board;

    public MoveProcessorImpl() {
        board = new Board();
    }

    @Override
    public boolean doMove(String currMove, String currBoardState, Side side) {

        Move move = prepareMove(currMove, side);
        board.loadFromFen(currBoardState);

        boolean isValid = board.doMove(move, true);
        LinkedList<Long> l = board.getHistory();
        System.out.println(l);
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
            if(!StringUtils.containsAnyIgnoreCase(promotion, "Q", "R", "B", "K")) {
                throw new InvalidMoveException("Invalid piece promotion!");
            }
        }
        return new Move(currMove, side);
    }
}
