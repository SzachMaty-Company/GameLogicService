package com.szachmaty.gamelogicservice.domain.game;

import com.szachmaty.gamelogicservice.domain.board.Board;
import com.szachmaty.gamelogicservice.domain.game.logic.PawnMove;
import com.szachmaty.gamelogicservice.domain.move.Move;

import java.util.Set;

public class PlayerMoveValidation {

    public boolean isMoveValid(Board board, Move move) {
        var currentBoardState = board.getCurrentBoardState();

        var pieceToMove = currentBoardState.getPieceOnPos(move.from());

        if (currentBoardState.isPosEmpty(move.to())) {
            var possibleMoves = switch (pieceToMove.getType()) {
                case PAWN -> PawnMove.getPawnMoves(board, move.from());
                default -> Set.of();
            };

            return possibleMoves.contains(move.to());
        }

        return false;
    }

}
