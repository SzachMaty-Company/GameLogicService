package com.szachmaty.gamelogicservice.domain.game.logic;

import com.szachmaty.gamelogicservice.domain.board.Board;
import com.szachmaty.gamelogicservice.domain.board.BoardState;
import com.szachmaty.gamelogicservice.domain.board.piece.PieceType;
import com.szachmaty.gamelogicservice.domain.move.Move;
import com.szachmaty.gamelogicservice.domain.move.Pos;
import com.szachmaty.gamelogicservice.domain.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static Set<Pos> getPawnCaptures(Board board, Pos pos) {
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

    private static Set<Pos> getPawnCapturesForWhite(Board board, Pos from) {
        var possibleCaptures = new ArrayList<Pos>();
        var currentBoardState = board.getCurrentBoardState();

        var normalCapture1 = Pos.of(from.file() - 1, from.rank() + 1);
        if (!currentBoardState.isPosEmpty(normalCapture1)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(normalCapture1).getPlayer())) {
            possibleCaptures.add(normalCapture1);
        }

        var normalCapture2 = Pos.of(from.file() + 1, from.rank() + 1);
        if (!currentBoardState.isPosEmpty(normalCapture2)
                && Player.BLACK.equals(currentBoardState.getPieceOnPos(normalCapture2).getPlayer())) {
            possibleCaptures.add(normalCapture2);
        }

        // en passant
        var pieceNextToPawn1 = currentBoardState.getPieceOnPos(Pos.of(from.file() - 1, from.rank()));
        if (currentBoardState.isPosEmpty(normalCapture1)
                && pieceNextToPawn1 != null
                && Player.BLACK.equals(pieceNextToPawn1.getPlayer())
                && pieceNextToPawn1.isCanBeCapturedByEnPassant()) {
            possibleCaptures.add(normalCapture1);
        }

        var pieceNextToPawn2 = currentBoardState.getPieceOnPos(Pos.of(from.file() + 1, from.rank()));
        if (currentBoardState.isPosEmpty(normalCapture1)
                && pieceNextToPawn2 != null
                && Player.BLACK.equals(pieceNextToPawn2.getPlayer())
                && pieceNextToPawn2.isCanBeCapturedByEnPassant()) {
            possibleCaptures.add(normalCapture2);
        }

        var captures =  possibleCaptures.stream()
                .filter(BoardState::isPosValid)
                .collect(Collectors.toSet());

        return captures;
    }

    private static Set<Move> getPawnMovesForBlack(Board board, Pos pos) {
        return null;
    }

    private static Set<Pos> getPawnCapturesForBlack(Board board, Pos pos) {
        return null;
    }

}
