package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoveValidatorImpl implements MoveValidator {

    private final GameDTOManager gameDTOManager;
    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final static int MOVE_LEN = 4;

    @Override
    public String validateMove(String currMove, String gameCode) {
        List<String> boards = gameDTOManager.getBoards(gameCode);
        Side side;
        String currBoardState;
        if(boards != null) {
            side = boards.size() % 2 == 0 ? Side.WHITE : Side.BLACK;
            currBoardState = boards.size() == 0 ? INIT_CHESS_BOARD : boards.get(boards.size() - 1);
        } else { //first move
            side = Side.WHITE;
            currBoardState = INIT_CHESS_BOARD;
        }

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
