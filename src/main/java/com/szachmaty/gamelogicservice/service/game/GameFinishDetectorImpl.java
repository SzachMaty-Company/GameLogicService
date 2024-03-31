package com.szachmaty.gamelogicservice.service.game;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameProcessDTO;
import com.szachmaty.gamelogicservice.data.dto.GameFinishDTO;
import com.szachmaty.gamelogicservice.exception.GameException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class GameFinishDetectorImpl implements GameFinishDetector {

    private final static int IS_WINNER = 1;
    private final static int NO_WINNER = 0;

    @Override
    public GameFinishDTO checkResultBasedOnBoard(GameProcessDTO gameProcessDTO) {
        LinkedList<Long> gameHistory = gameProcessDTO.getGameHistory();
        String boardState = gameProcessDTO.getAfterMoveBoardState();
        Side side = gameProcessDTO.getSide();
        boolean isWhiteWinner = false;
        boolean isBlackWinner = false;
        boolean isDraw;

        Board board = new Board();
        if(gameHistory != null) {
            for (var game : gameHistory) {
                board.getHistory().add(game);
            }
        }
        board.loadFromFen(boardState);
        isDraw = board.isDraw();
        boolean isMate = board.isMated();

        if(isMate && side.equals(Side.WHITE)) {
            isWhiteWinner = true;
        } else if(isMate && side.equals(Side.BLACK)) {
            isBlackWinner = true;
        }
        boolean isFinish = isWhiteWinner || isBlackWinner || isDraw;
        return new GameFinishDTO(isWhiteWinner, isBlackWinner, isDraw, isFinish);
    }

    public GameFinishDTO checkResultBasedOnTime(GameProcessDTO gameProcessDTO) {
        String boardState = gameProcessDTO.getAfterMoveBoardState();
        Long whiteTime = gameProcessDTO.getWhiteTime();
        Long blackTime = gameProcessDTO.getBlackTime();
        boolean isWhiteWinner = false;
        boolean isBlackWinner = false;
        boolean isDraw = false;

        Board board = new Board();
        board.loadFromFen(boardState);
        Piece[] pieces = board.boardToArray();

        if(whiteTime != null && whiteTime < 0) {
            int[] resultArray = blackPlayerInsufficientMaterialToWinChecker(pieces);
            if(resultArray.length != 4) {
                throw new RuntimeException("An error occurred while checking the game's completion status!");
            }
            isBlackWinner = resultArray[0] == IS_WINNER;
            if(!isBlackWinner) {
                isDraw = determineIsDraw(resultArray);
                isBlackWinner = !isDraw;
            }
        }
        else if(blackTime != null && blackTime < 0) {
            int[] resultArray = whitePlayerInsufficientMaterialToWinChecker(pieces);
            if(resultArray.length != 4) {
                throw new GameException("An error occurred while checking the game's completion status!");
            }
            isWhiteWinner = resultArray[0] == IS_WINNER;
            if(!isWhiteWinner) {
                isDraw = determineIsDraw(resultArray);
                isWhiteWinner = !isDraw;
            }

        }
        boolean isFinish = isWhiteWinner || isBlackWinner || isDraw;
        return new GameFinishDTO(isWhiteWinner, isBlackWinner, isDraw, isFinish);
    }

    private boolean determineIsDraw(int[] resultArray) {
        boolean isDraw = false;
        int bishopCounter = resultArray[1];
        int knightCounter = resultArray[2];
        int oponentPieceCounter = resultArray[3];
        if(bishopCounter == 0 && knightCounter == 0) {
            isDraw = true;
        }
        if(bishopCounter == 1 || knightCounter == 1) {
            isDraw = true;
        }
        if(knightCounter == 2 && oponentPieceCounter == 0) {
            isDraw = true;
        }
        return isDraw;
    }

    private int[] whitePlayerInsufficientMaterialToWinChecker(Piece[] pieces) {
        boolean isWhiteWinner = false;
        int bishopCounter = 0;
        int knightCounter = 0;
        int blackPieceCounter = 0;
        for(var p : pieces) {
            if(p.equals(Piece.WHITE_PAWN) || p.equals(Piece.WHITE_QUEEN) || p.equals(Piece.WHITE_ROOK)) {
                isWhiteWinner = true;
                break;
            } else if(p.equals(Piece.WHITE_BISHOP)) {
                bishopCounter++;
            } else if(p.equals(Piece.WHITE_KNIGHT)) {
                knightCounter++;
            }
            if(p.equals(Piece.BLACK_BISHOP) || p.equals(Piece.BLACK_KNIGHT) ||
                    p.equals(Piece.BLACK_PAWN) || p.equals(Piece.BLACK_QUEEN) || p.equals(Piece.BLACK_ROOK)) {
                blackPieceCounter++;
            }
        }
        return new int[]{isWhiteWinner ? IS_WINNER : NO_WINNER, bishopCounter, knightCounter, blackPieceCounter};
    }

    private int[] blackPlayerInsufficientMaterialToWinChecker(Piece[] pieces) {
        boolean isBlackWinner = false;
        int bishopCounter = 0;
        int knightCounter = 0;
        int whitePieceCounter = 0;
        for(var p : pieces) {
            if(p.equals(Piece.BLACK_PAWN) || p.equals(Piece.BLACK_QUEEN) || p.equals(Piece.BLACK_ROOK)) {
                isBlackWinner = true;
                break;
            } else if(p.equals(Piece.BLACK_BISHOP)) {
                bishopCounter++;
            } else if(p.equals(Piece.BLACK_KNIGHT)) {
                knightCounter++;
            }
            if(p.equals(Piece.WHITE_BISHOP) || p.equals(Piece.WHITE_KNIGHT) ||
                    p.equals(Piece.WHITE_PAWN) || p.equals(Piece.WHITE_QUEEN) || p.equals(Piece.WHITE_ROOK)) {
                whitePieceCounter++;
            }
        }
        return new int[]{isBlackWinner ? IS_WINNER : NO_WINNER, bishopCounter, knightCounter, whitePieceCounter};
    }
}
