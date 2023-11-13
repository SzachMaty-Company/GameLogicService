package com.szachmaty.gamelogicservice.domain.board;

import com.szachmaty.gamelogicservice.domain.board.piece.Piece;
import com.szachmaty.gamelogicservice.domain.move.Move;
import com.szachmaty.gamelogicservice.domain.move.Pos;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardState {

    private List<Piece> pieces;

    public BoardState(List<Piece> pieces) {
        this.pieces = pieces;
    }

    public Piece getPieceOnPos(Pos pos) {
        return pieces.stream()
                .filter(p -> p.getPos().equals(pos))
                .findFirst()
                .orElse(null);
    }

    public static BoardState makeMove(BoardState boardState, Move move) {
        return null;
    }

}
