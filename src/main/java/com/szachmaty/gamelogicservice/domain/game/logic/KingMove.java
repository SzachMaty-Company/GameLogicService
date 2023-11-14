package com.szachmaty.gamelogicservice.domain.game.logic;

import com.szachmaty.gamelogicservice.domain.board.Board;
import com.szachmaty.gamelogicservice.domain.board.piece.PieceType;
import com.szachmaty.gamelogicservice.domain.move.Move;
import com.szachmaty.gamelogicservice.domain.move.Pos;

import java.util.Set;

public class KingMove {

    private final static Pos WHITE_KING_POS = Pos.of("e1");
    private final static Pos WHITE_KING_ROOK_POS = Pos.of("h1");
    private final static Pos WHITE_QUEEN_ROOK_POS = Pos.of("a1");

    private final static Pos BLACK_KING_POS = Pos.of("e8");
    private final static Pos BLACK_KING_ROOK_POS = Pos.of("h8");
    private final static Pos BLACK_QUEEN_ROOK_POS = Pos.of("a8");

    public static Set<Move> getKingMoves(Board board, Pos from) {
        if (!PieceType.KING.equals(board.getCurrentBoardState().getPieceOnPos(from).getType())) {
            return Set.of();
        }

        return switch (board.getCurrentBoardState().getPlayerToMove()) {
            case WHITE -> getKingMovesForWhite(board, from);
            case BLACK -> getKingMovesForBlack(board, from);
        };
    }

    private static Set<Move> getKingMovesForWhite(Board board, Pos from) {

        return null;
    }

    private static Set<Move> getKingMovesForBlack(Board board, Pos from) {
        return null;
    }

}
