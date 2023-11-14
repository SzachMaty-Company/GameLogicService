package com.szachmaty.gamelogicservice.domain.board.piece;

public enum PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    public static PieceType fromLabel(char label) {
        char labelInLowerCase = Character.toLowerCase(label);

        return switch (labelInLowerCase) {
            case 'p' -> PAWN;
            case 'b' -> BISHOP;
            case 'n' -> KNIGHT;
            case 'r' -> ROOK;
            case 'k' -> KING;
            case 'q' -> QUEEN;
            default -> throw new IllegalStateException("Invalid piece label provided: " + label);
        };
    }
}
