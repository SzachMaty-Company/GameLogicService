package com.szachmaty.gamelogicservice.domain.game.logic;

import com.szachmaty.gamelogicservice.domain.board.Board;
import com.szachmaty.gamelogicservice.domain.board.piece.PieceType;
import com.szachmaty.gamelogicservice.domain.move.Pos;
import com.szachmaty.gamelogicservice.domain.player.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class PawnMove {

    public static Set<Pos> getPawnMoves(Board board, Pos pos) {
        if (!PieceType.PAWN.equals(board.getCurrentBoardState().getPieceOnPos(pos).getType())) {
            return Set.of();
        }

        return switch (board.getCurrentBoardState().getPlayerToMove()) {
            case WHITE -> getPawnMovesForWhite(board, pos);
            case BLACK -> getPawnMovesForBlack(board, pos);
        };
    }

    public static Set<Pos> getPawnCaptures(Board board, Pos pos) {
        if (!PieceType.PAWN.equals(board.getCurrentBoardState().getPieceOnPos(pos).getType())) {
            return Set.of();
        }

        return switch (board.getCurrentBoardState().getPlayerToMove()) {
            case WHITE -> getPawnCapturesForWhite(board, pos);
            case BLACK -> getPawnCapturesForBlack(board, pos);
        };
    }

    private static Set<Pos> getPawnMovesForWhite(Board board, Pos pos) {
        var possibleMovesWithoutCapture = new ArrayList<Pos>();
        var currentBoardState = board.getCurrentBoardState();

        // moves
        var shortMoveNextPos = Pos.of(pos.x(), pos.y() + 1);
        if (currentBoardState.isPosEmpty(shortMoveNextPos)) {
            possibleMovesWithoutCapture.add(shortMoveNextPos);
        }

        var longMoveNextPos = Pos.of(pos.x(), pos.y() + 2);
        if (!currentBoardState.getPieceOnPos(pos).isHasMoved()
                && currentBoardState.isPosEmpty(shortMoveNextPos)
                && currentBoardState.isPosEmpty(longMoveNextPos)) {
            possibleMovesWithoutCapture.add(longMoveNextPos);
        }

        var moves = possibleMovesWithoutCapture.stream()
                .filter(Pos::isPosValid)
                .collect(Collectors.toSet());

        return moves;
    }

    private static Set<Pos> getPawnCapturesForWhite(Board board, Pos pos) {
        var possibleCaptures = new ArrayList<Pos>();
        var currentBoardState = board.getCurrentBoardState();

        var normalCapture1 = Pos.of(pos.x() + 1, pos.y() + 1);
        if (!currentBoardState.isPosEmpty(normalCapture1)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(normalCapture1).getPlayer())) {
            possibleCaptures.add(normalCapture1);

        }

        var normalCapture2 = Pos.of(pos.x() - 1, pos.y() + 1);
        if (!currentBoardState.isPosEmpty(normalCapture2)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(normalCapture2).getPlayer())) {
            possibleCaptures.add(normalCapture2);

        }

        // en passant
        var pieceNextToPawn1 = currentBoardState.getPieceOnPos(Pos.of(pos.x() + 1, pos.y()));
        if (currentBoardState.isPosEmpty(normalCapture1)
                && pieceNextToPawn1 != null
                && Player.BLACK.equals(pieceNextToPawn1.getPlayer())
                && pieceNextToPawn1.isCanBeCapturedByEnPassant()) {
            possibleCaptures.add(normalCapture1);
        }

        var pieceNextToPawn2 = currentBoardState.getPieceOnPos(Pos.of(pos.x() - 1, pos.y()));
        if (currentBoardState.isPosEmpty(normalCapture1)
                && pieceNextToPawn2 != null
                && Player.BLACK.equals(pieceNextToPawn2.getPlayer())
                && pieceNextToPawn2.isCanBeCapturedByEnPassant()) {
            possibleCaptures.add(normalCapture2);
        }

        var captures =  possibleCaptures.stream()
                .filter(Pos::isPosValid)
                .collect(Collectors.toSet());

        return captures;
    }

    private static Set<Pos> getPawnMovesForBlack(Board board, Pos pos) {
        return null;
    }

    private static Set<Pos> getPawnCapturesForBlack(Board board, Pos pos) {
        return null;
    }

}
