package com.szachmaty.gamelogicservice.domain.board;

import com.szachmaty.gamelogicservice.domain.board.piece.Piece;
import com.szachmaty.gamelogicservice.domain.move.Move;
import com.szachmaty.gamelogicservice.domain.move.Pos;
import com.szachmaty.gamelogicservice.domain.player.Player;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardState {

    private List<Piece> pieces;
    private Player playerToMove;

    public BoardState(List<Piece> pieces, Player playerToMove) {
        this.pieces = pieces;
        this.playerToMove = playerToMove;
    }

    public Piece getPieceOnPos(Pos pos) {
        return pieces.stream()
                .filter(p -> p.getPos().equals(pos))
                .findFirst()
                .orElse(null);
    }

    public static boolean isPosValid(Pos pos) {
        var isFileValid = pos.file() >= 1 && pos.file() <= 8;
        var isRankValid = pos.rank() >= 1 && pos.rank() <= 8;

        return isFileValid && isRankValid;
    }

    public static BoardState makeMove(BoardState boardState, Move move) {
        return null;
    }

    public boolean isPosEmpty(Pos pos) {
        return pos == null || getPieceOnPos(pos) == null;
    }

    public boolean isPosOnWhiteBackRank(Pos pos) {
        return pos.rank() == 1;
    }

    public boolean isPosOnBlackBackRank(Pos pos) {
        return pos.rank() == 8;
    }
}
