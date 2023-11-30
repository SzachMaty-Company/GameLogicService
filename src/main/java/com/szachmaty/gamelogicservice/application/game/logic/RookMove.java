package com.szachmaty.gamelogicservice.application.game.logic;

import com.szachmaty.gamelogicservice.application.board.Board;
import com.szachmaty.gamelogicservice.application.board.BoardState;
import com.szachmaty.gamelogicservice.application.move.piece.PieceType;
import com.szachmaty.gamelogicservice.application.move.Pos;
import com.szachmaty.gamelogicservice.application.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RookMove {

    public static Set<Pos> getRookMoves(Board board, Pos pos) {
        if (!PieceType.ROOK.equals(board.getCurrentBoardState().getPieceOnPos(pos).getType())) {
            return Set.of();
        }

        return switch (board.getCurrentBoardState().getPlayerToMove()) {
            case WHITE -> getRookMovesForWhite(board, pos);
            case BLACK -> getRookMovesForBlack(board, pos);
        };
    }

    public static Set<Pos> getRookCaptures(Board board, Pos pos) {
        if (!PieceType.ROOK.equals(board.getCurrentBoardState().getPieceOnPos(pos).getType())) {
            return Set.of();
        }

        return switch (board.getCurrentBoardState().getPlayerToMove()) {
            case WHITE -> getRookCapturesForWhite(board, pos);
            case BLACK -> getRookCapturesForBlack(board, pos);
        };
    }

    private static Set<Pos> getRookMovesForWhite(Board board, Pos pos) {
        var possibleMovesWithoutCapture = new ArrayList<Pos>();
        var currentBoardState = board.getCurrentBoardState();

        possibleMovesWithoutCapture.addAll(getPosInDirection(currentBoardState, i -> Pos.of(pos.file() + i, pos.rank())));
        possibleMovesWithoutCapture.addAll(getPosInDirection(currentBoardState, i -> Pos.of(pos.file() - i, pos.rank())));
        possibleMovesWithoutCapture.addAll(getPosInDirection(currentBoardState, i -> Pos.of(pos.file(), pos.rank() + i)));
        possibleMovesWithoutCapture.addAll(getPosInDirection(currentBoardState, i -> Pos.of(pos.file(), pos.rank() - i)));

        var moves = possibleMovesWithoutCapture.stream()
                .filter(BoardState::isPosValid)
                .collect(Collectors.toSet());

        return moves;
    }

    private static Set<Pos> getRookCapturesForWhite(Board board, Pos pos) {
        var possibleCaptures = new ArrayList<Pos>();
        var currentBoardState = board.getCurrentBoardState();

        possibleCaptures.add(getCaptureInDirection(currentBoardState, i -> Pos.of(pos.file() + i, pos.rank())));
        possibleCaptures.add(getCaptureInDirection(currentBoardState, i -> Pos.of(pos.file() - i, pos.rank())));
        possibleCaptures.add(getCaptureInDirection(currentBoardState, i -> Pos.of(pos.file(), pos.rank() + i)));
        possibleCaptures.add(getCaptureInDirection(currentBoardState, i -> Pos.of(pos.file(), pos.rank() - i)));

        var captures = possibleCaptures.stream()
                .filter(Objects::nonNull)
                .filter(BoardState::isPosValid)
                .filter(p -> Player.BLACK.equals(currentBoardState.getPieceOnPos(pos).getPlayer()))
                .collect(Collectors.toSet());

        return captures;
    }

    private static Set<Pos> getRookMovesForBlack(Board board, Pos pos) {
        return null;
    }

    private static Set<Pos> getRookCapturesForBlack(Board board, Pos pos) {
        return null;
    }

    private static List<Pos> getPosInDirection(BoardState boardState, IntFunction<Pos> dirFunction) {
        return IntStream.range(1, 8)
                .mapToObj(dirFunction)
                .takeWhile(boardState::isPosEmpty)
                .toList();
    }

    private static Pos getCaptureInDirection(BoardState boardState, IntFunction<Pos> dirFunction) {
        return IntStream.range(1, 8)
                .mapToObj(dirFunction)
                .dropWhile(boardState::isPosEmpty)
                .findFirst()
                .orElse(null);
    }

}
