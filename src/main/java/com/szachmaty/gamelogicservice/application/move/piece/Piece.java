package com.szachmaty.gamelogicservice.application.move.piece;

import com.szachmaty.gamelogicservice.application.move.Pos;
import com.szachmaty.gamelogicservice.application.player.Player;
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

    public boolean canBeCapturedByEnPassant() {
        return canBeCapturedByEnPassant;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

}
