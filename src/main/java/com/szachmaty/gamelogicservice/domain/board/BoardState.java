package com.szachmaty.gamelogicservice.domain.board;

import com.szachmaty.gamelogicservice.domain.board.piece.Piece;
import com.szachmaty.gamelogicservice.domain.board.piece.PieceType;
import com.szachmaty.gamelogicservice.domain.move.Move;
import com.szachmaty.gamelogicservice.domain.move.Pos;
import com.szachmaty.gamelogicservice.domain.player.Player;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardState {

    private List<Piece> pieces;
    private Player playerToMove;

    public BoardState(List<Piece> pieces, Player playerToMove) {
        this.pieces = pieces;
        this.playerToMove = playerToMove;
    }

    public Piece getPieceOnPos(Pos pos) {
        return pieces.stream()
                .filter(p -> p.getPos().equals(pos))
                .findFirst()
                .orElse(null);
    }

    public static boolean isPosValid(Pos pos) {
        var isFileValid = pos.file() >= 1 && pos.file() <= 8;
        var isRankValid = pos.rank() >= 1 && pos.rank() <= 8;

        return isFileValid && isRankValid;
    }

    public static BoardState makeMove(BoardState boardState, Move move) {
        return null;
    }

    public boolean isPosEmpty(Pos pos) {
        return pos == null || getPieceOnPos(pos) == null;
    }

    // whites start from 1st rank
    public boolean isPosOnWhiteBackRank(Pos pos) {
        return pos.rank() == 1;
    }

    // blacks start from 8th rank
    public boolean isPosOnBlackBackRank(Pos pos) {
        return pos.rank() == 8;
    }

    @Override
    public String toString() {
        final var BOARD_SIZE = 8;
        var arrayBoard = new String[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                arrayBoard[i][j] = ".";
            }
        }

        for (var piece : pieces) {
            var i = piece.getPos().rank() - 1;
            var j = piece.getPos().file() - 1;

            var label = PieceType.toLabel(piece.getType());

            arrayBoard[j][i] = piece.getPlayer() == Player.WHITE ? label.toUpperCase() : label.toLowerCase();
        }

        var boardString = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            boardString.append(8 - i);
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardString.append(arrayBoard[j][i]);
                boardString.append(" ");
            }
            boardString.append("\n");
        }

        boardString.append(" A B C D E F G H");

        return boardString.toString();
    }
}
