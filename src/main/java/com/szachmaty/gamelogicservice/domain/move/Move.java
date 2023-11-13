package com.szachmaty.gamelogicservice.domain.move;

import com.szachmaty.gamelogicservice.domain.move.exception.IllegalMoveException;

public class Move {

    private Pos from;
    private Pos to;

    public Move(Pos from, Pos to) {
        this.from = from;
        this.to = to;
    }

    public static Move of(Pos from, Pos to) {
        var arePosTheSame = from.equals(to);
        if (arePosTheSame) {
            throw new IllegalMoveException("From field and To field must be different");
        }

        return new Move(from, to);
    }

    public static Move of(String move) {
        var isMoveNotNull = move != null;
        if (!isMoveNotNull) {
            throw new IllegalArgumentException("Move cannot be null");
        }

        var isLengthValid = move.length() == 4;
        if (!isLengthValid) {
            throw new IllegalArgumentException("Invalid move format");
        }

        var from = move.substring(0, 2);
        var to = move.substring(2, 4);

        var fromPos = Pos.of(from);
        var toPos = Pos.of(to);

        return new Move(fromPos, toPos);
    }
}
