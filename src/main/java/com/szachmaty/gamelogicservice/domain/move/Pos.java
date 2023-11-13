package com.szachmaty.gamelogicservice.domain.move;

import com.szachmaty.gamelogicservice.domain.move.exception.InvalidPosException;

public record Pos(int x, int y) {

    private static final String LABELS = "abcdefgh";

    public Pos {
        var isXValid = x >= 0 && x < 8;
        var isYValid = y >= 0 && y < 8;

        if (!isXValid || !isYValid) {
            throw new InvalidPosException("Invalid pos value: " + this.toSimpleString());
        }
    }

    public static Pos of(int x, int y) {
        return new Pos(x, y);
    }

    public static Pos of(String pos) {
        var isStringNotNull = pos != null;
        if (!isStringNotNull) {
            throw new IllegalArgumentException("Pos cannot be constructed from null");
        }

        var isLengthValid = pos.length() == 2;
        if (!isLengthValid) {
            throw new IllegalArgumentException("Length must be 2");
        }

        var label = pos.substring(0, 1).toLowerCase().toCharArray()[0];
        var x = LABELS.indexOf(label);
        var isCharacterValid = x != -1;
        if (!isCharacterValid) {
            throw new IllegalArgumentException("Invalid Character at index 0");
        }

        int y = 0;
        try {
            y = Integer.parseInt(String.valueOf(pos.charAt(1)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid character at index 1");
        }

        var isYValid = y >= 0 && y < 8;
        if (!isYValid) {
            throw new IllegalArgumentException("Invalid number at index 1");
        }

        return new Pos(x, y);
    }

    public String toSimpleString() {
        return this.x + ", " + this.y;
    }

    public String toCanonicalString() {
        return xToLabel() + this.y;
    }

    private String xToLabel() {
        return String.valueOf(LABELS.charAt(x));
    }

}
