package com.szachmaty.gamelogicservice.application.move;

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

        return new Pos(file, rank);
    }

    public String toSimpleString() {
        return this.file + ", " + this.rank;
    }

    public String toCanonicalString() {
        return toFile() + this.rank;
    }

    private String toFile() {
        return String.valueOf(FILES.charAt(file));
    }

}
