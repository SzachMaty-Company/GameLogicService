package com.szachmaty.gamelogicservice.domain.board.piece;

import com.szachmaty.gamelogicservice.domain.move.Pos;
import com.szachmaty.gamelogicservice.domain.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Piece {

    private PieceType type;
    private Pos pos;
    private Player player;
    private boolean canBeCapturedByEnPassant;
    private boolean hasMoved;

    public Piece(PieceType type, Pos pos, Player player) {
        this.type = type;
        this.pos = pos;
        this.player = player;
        this.canBeCapturedByEnPassant = false;
        this.hasMoved = false;
    }

}
