package com.szachmaty.gamelogicservice.application.move;

public record Capture(Move move, Pos posOfPieceToCapture) {
    public static Capture of(Move move) {
        return new Capture(move, move.to());
    }

    public static Capture enPassant(Move move, Pos posOfPieceToCapture) {
        return new Capture(move, posOfPieceToCapture);
    }
}
