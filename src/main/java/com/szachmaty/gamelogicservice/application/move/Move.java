package com.szachmaty.gamelogicservice.application.move;

import com.szachmaty.gamelogicservice.application.move.piece.PieceType;
import com.szachmaty.gamelogicservice.application.move.exception.IllegalMoveException;

// TODO: what about pawn promotion?
public record Move(Pos from, Pos to, PieceType pieceType) {

    public static Move of(Pos from, Pos to) {
        throwIfPositionsAreTheSame(from, to);

        return new Move(from, to, null);
    }

    public static Move withPromotion(Pos from, Pos to, PieceType pieceType) {
        throwIfPositionsAreTheSame(from, to);

        return new Move(from, to, pieceType);
    }

    public static Move of(String move) {
        var isMoveNotNull = move != null;
        if (!isMoveNotNull) {
            throw new IllegalArgumentException("Move cannot be null");
        }

        var fromString = move.substring(0, 2);
        var toString = move.substring(2, 4);

        var from = Pos.of(fromString);
        var to = Pos.of(toString);

        throwIfPositionsAreTheSame(from, to);

        // with promotion
        if (move.length() == 5) {
            return new Move(from, to, PieceType.fromLabel(move.charAt(4)));
        }

        return new Move(from, to, null);
    }

    private static void throwIfPositionsAreTheSame(Pos from, Pos to) {
        var arePosTheSame = from.equals(to);
        if (arePosTheSame) {
            throw new IllegalMoveException("From field and To field must be different");
        }
    }
}
