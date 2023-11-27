package com.szachmaty.gamelogicservice.domain.game.logic;

import com.szachmaty.gamelogicservice.domain.board.Board;
import com.szachmaty.gamelogicservice.domain.move.piece.PieceType;
import com.szachmaty.gamelogicservice.domain.move.Move;
import com.szachmaty.gamelogicservice.domain.move.Pos;

import java.util.Objects;
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

    private boolean isKingSafe(Board board) {
        var king = board.getCurrentBoardState().getPieces().stream()
                .filter(p -> p.getType() == PieceType.KING)
                .filter(p -> p.getPlayer() == board.getCurrentBoardState().getPlayerToMove())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("King not found on the board"));

        var possibleAttackingPieces = board.getCurrentBoardState().getPieces().stream()
                .filter(p -> p.getType() != PieceType.KING)
                .filter(p -> p.getPlayer() != board.getCurrentBoardState().getPlayerToMove())
                .toList();

        var isKingSafe = possibleAttackingPieces.stream()
                .map(p -> PieceMove.getPieceCaptures(board, p.getPos()))
                .filter(Objects::nonNull)
                .noneMatch(captures -> captures.contains(king.getPos()));

        return isKingSafe;
    }

    private static Set<Move> getKingMovesForWhite(Board board, Pos from) {

        return null;
    }

    private static Set<Move> getKingMovesForBlack(Board board, Pos from) {
        return null;
    }

}
