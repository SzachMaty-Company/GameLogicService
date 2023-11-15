package com.szachmaty.gamelogicservice.domain.board.piece;

import java.util.List;

public enum PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    public static PieceType fromLabel(char label) {
        char labelInLowerCase = Character.toLowerCase(label);

        return switch (labelInLowerCase) {
            case 'p' -> PAWN;
            case 'n' -> KNIGHT;
            case 'b' -> BISHOP;
            case 'r' -> ROOK;
            case 'k' -> KING;
            case 'q' -> QUEEN;
            default -> throw new IllegalStateException("Invalid piece label provided: " + label);
        };
    }

    public static String toLabel(PieceType type) {
        return switch (type) {
            case PAWN -> "p";
            case KNIGHT -> "n";
            case BISHOP -> "b";
            case ROOK -> "r";
            case QUEEN -> "q";
            case KING -> "k";
        };
    }

    public static final List<PieceType> POSSIBLE_PROMOTION_PIECES = List.of(KNIGHT, BISHOP, ROOK, QUEEN);
}
