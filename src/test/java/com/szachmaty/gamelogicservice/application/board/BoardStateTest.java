package com.szachmaty.gamelogicservice.application.board;

import com.szachmaty.gamelogicservice.application.move.piece.Piece;
import com.szachmaty.gamelogicservice.application.move.piece.PieceType;
import com.szachmaty.gamelogicservice.application.move.Pos;
import com.szachmaty.gamelogicservice.application.player.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

class BoardStateTest {

    @Test
    void givenBoardState_whenToString_thenSuccess() {
        // given
        var pieces = List.of(
                new Piece(PieceType.ROOK, Pos.of("A1"), Player.WHITE),
                new Piece(PieceType.KING, Pos.of("B1"), Player.WHITE),
                new Piece(PieceType.PAWN, Pos.of("C1"), Player.WHITE),
                new Piece(PieceType.BISHOP, Pos.of("D1"), Player.WHITE)
        );
        var boardState = new BoardState(pieces, Player.WHITE);

        System.out.println(boardState.toString());
    }

}