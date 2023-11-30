package com.szachmaty.gamelogicservice.application.game.logic;

import com.szachmaty.gamelogicservice.application.board.Board;
import com.szachmaty.gamelogicservice.application.board.BoardState;
import com.szachmaty.gamelogicservice.application.move.piece.PieceType;
import com.szachmaty.gamelogicservice.application.move.Capture;
import com.szachmaty.gamelogicservice.application.move.Move;
import com.szachmaty.gamelogicservice.application.move.Pos;
import com.szachmaty.gamelogicservice.application.player.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class PawnMove {

    public static Set<Move> getPawnMoves(Board board, Pos pos) {
        if (!PieceType.PAWN.equals(board.getCurrentBoardState().getPieceOnPos(pos).getType())) {
            return Set.of();
        }

        return switch (board.getCurrentBoardState().getPlayerToMove()) {
            case WHITE -> getPawnMovesForWhite(board, pos);
            case BLACK -> getPawnMovesForBlack(board, pos);
        };
    }

    public static Set<Capture> getPawnCaptures(Board board, Pos pos) {
        if (!PieceType.PAWN.equals(board.getCurrentBoardState().getPieceOnPos(pos).getType())) {
            return Set.of();
        }

        return switch (board.getCurrentBoardState().getPlayerToMove()) {
            case WHITE -> getPawnCapturesForWhite(board, pos);
            case BLACK -> getPawnCapturesForBlack(board, pos);
        };
    }

    private static Set<Move> getPawnMovesForWhite(Board board, Pos from) {
        var possibleNextPositions = new ArrayList<Pos>();
        var movesWithPromotion = new ArrayList<Move>();
        var currentBoardState = board.getCurrentBoardState();

        // moves
        var shortMoveNextPos = Pos.of(from.file() + 1, from.rank());
        if (currentBoardState.isPosEmpty(shortMoveNextPos)
                && !currentBoardState.isPosOnBlackBackRank(shortMoveNextPos)) {
            possibleNextPositions.add(shortMoveNextPos);
        }

        // add moves with promotion
        if (currentBoardState.isPosEmpty(shortMoveNextPos)
                && currentBoardState.isPosOnBlackBackRank(shortMoveNextPos)) {
            var moves = PieceType.POSSIBLE_PROMOTION_PIECES.stream()
                    .map(type -> Move.withPromotion(from, shortMoveNextPos, type))
                    .toList();

            movesWithPromotion.addAll(moves);
        }

        var longMoveNextPos = Pos.of(from.file() + 2, from.rank());
        if (!currentBoardState.getPieceOnPos(from).isHasMoved()
                && currentBoardState.isPosEmpty(shortMoveNextPos)
                && currentBoardState.isPosEmpty(longMoveNextPos)) {
            possibleNextPositions.add(longMoveNextPos);
        }

        var moves = possibleNextPositions.stream()
                .filter(BoardState::isPosValid)
                .map(to -> Move.of(from, to))
                .collect(Collectors.toSet());

        moves.addAll(movesWithPromotion);

        return moves;
    }

    private static Set<Capture> getPawnCapturesForWhite(Board board, Pos from) {
        var possibleCaptures = new ArrayList<Capture>();
        var currentBoardState = board.getCurrentBoardState();

        var normalCapture1 = Pos.of(from.file() - 1, from.rank() + 1);
        if (!currentBoardState.isPosEmpty(normalCapture1)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(normalCapture1).getPlayer())) {
            possibleCaptures.add(Capture.of(Move.of(from, normalCapture1)));
        }

        var normalCapture2 = Pos.of(from.file() + 1, from.rank() + 1);
        if (!currentBoardState.isPosEmpty(normalCapture2)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(normalCapture2).getPlayer())) {
            possibleCaptures.add(Capture.of(Move.of(from, normalCapture2)));
        }

        // en passant
        var posNextToPawn1 = Pos.of(from.file() - 1, from.rank());
        if (currentBoardState.isPosEmpty(normalCapture1)
                && !currentBoardState.isPosEmpty(posNextToPawn1)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(posNextToPawn1).getPlayer())
                && currentBoardState.getPieceOnPos(posNextToPawn1).canBeCapturedByEnPassant()) {
            possibleCaptures.add(Capture.enPassant(Move.of(from, normalCapture1), posNextToPawn1));
        }

        var posNextToPawn2 = Pos.of(from.file() + 1, from.rank());
        if (currentBoardState.isPosEmpty(normalCapture1)
                && !currentBoardState.isPosEmpty(posNextToPawn2)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(posNextToPawn2).getPlayer())
                && currentBoardState.getPieceOnPos(posNextToPawn2).canBeCapturedByEnPassant()) {
            possibleCaptures.add(Capture.enPassant(Move.of(from, normalCapture2), posNextToPawn2));
        }

        var captures = possibleCaptures.stream()
                .filter(c -> BoardState.isPosValid(c.move().to()))
                .filter(c -> BoardState.isPosValid(c.posOfPieceToCapture()))
                .collect(Collectors.toSet());

        return captures;
    }

    private static Set<Move> getPawnMovesForBlack(Board board, Pos pos) {
        return null;
    }

    private static Set<Capture> getPawnCapturesForBlack(Board board, Pos pos) {
        return null;
    }

}
