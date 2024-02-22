package com.szachmaty.gamelogicservice.application.game;

import com.github.bhlangonijr.chesslib.Board;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveValidatorImpl implements MoveValidator {

    private final GameDTOManager gameDTOManager;

    @Override
    public String validateMove(String currMove, String gameCode) {
        String prevBoardState = gameDTOManager.getCurrBoardStateByGameCode(gameCode);
        Board board = new Board();
        board.loadFromFen(prevBoardState);
        boolean isValid = board.doMove(currMove); //tu będzie checkowanie
        if(isValid) {
            return board.getFen();
        } else {
            throw new InvalidMoveException("Move: " + currMove + " is invalid!");
        }
    }
}
