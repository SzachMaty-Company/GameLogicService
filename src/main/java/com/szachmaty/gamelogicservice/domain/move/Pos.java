package com.szachmaty.gamelogicservice.domain.move;

import com.szachmaty.gamelogicservice.domain.move.exception.InvalidPosException;

public record Pos(int file, int rank) {

    private static final String FILES = "abcdefgh";

    public static Pos of(int file, int rank) {
        return new Pos(file, rank);
    }

    public static Pos of(String value) {
        var isValueNotNull = value != null;
        if (!isValueNotNull) {
            throw new IllegalArgumentException("Pos cannot be constructed from null");
        }

        var isLengthValid = value.length() == 2;
        if (!isLengthValid) {
            throw new IllegalArgumentException("Length must be 2");
        }

        var file = FILES.indexOf(Character.toLowerCase(value.charAt(0))) + 1;
        var isCharacterValid = file != 0;
        if (!isCharacterValid) {
            throw new IllegalArgumentException("Invalid Character at index 0");
        }

        int rank = 1;
        try {
            rank = Integer.parseInt(String.valueOf(value.charAt(1)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid character at index 1");
        }

        var pos = new Pos(file, rank);
        if (!isPosValid(pos)) {
            throw new InvalidPosException("Pos is invalid: " + pos.toSimpleString());
        }
        return pos;
    }

    public String toSimpleString() {
        return this.file + ", " + this.rank;
    }

    public String toCanonicalString() {
        return xToLabel() + this.rank;
    }

    public static boolean isPosValid(Pos pos) {
        var isFileValid = pos.file >= 1 && pos.file <= 8;
        var isRankValid = pos.rank >= 1 && pos.rank <= 8;

        return isFileValid && isRankValid;
    }

    private String xToLabel() {
        return String.valueOf(FILES.charAt(file));
    }

}
