package com.szachmaty.gamelogicservice.service.game.chain.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class GameFinishDetectorImpl implements GameFinishDetector {

    private final static int IS_WINNER = 1;
    private final static int NO_WINNER = 0;

    @Override
    public GameStatus checkResultBasedOnBoard(GameProcessContext gameProcessContext) {
        LinkedList<Long> gameHistory = gameProcessContext.getGameHistory();
        String boardState = gameProcessContext.getNextFen();
        Side side = gameProcessContext.getSide();
        boolean isDraw;
        GameStatus gameStatus = GameStatus.IN_GAME;

        Board board = new Board();
        if(gameHistory != null) {
            for (var game : gameHistory) {
                board.getHistory().add(game);
            }
        }
        board.loadFromFen(boardState);
        isDraw = board.isDraw();
        if(isDraw) {
            gameStatus = GameStatus.DRAW;
        }

        boolean isMate = board.isMated();
        if(isMate && side.equals(Side.WHITE)) {
            gameStatus = GameStatus.WHITE_WINNER;
        } else if(isMate && side.equals(Side.BLACK)) {
            gameStatus = GameStatus.BLACK_WINNER;
        }

        return gameStatus;
    }

    public GameStatus checkResultBasedOnTime(GameProcessContext gameProcessContext) {
        String boardState = gameProcessContext.getNextFen();
        Long whiteTime = gameProcessContext.getWhiteTime();
        Long blackTime = gameProcessContext.getBlackTime();
        GameStatus gameStatus = GameStatus.IN_GAME;
        boolean isWhiteWinner = false;
        boolean isBlackWinner = false;
        boolean isDraw = false;

        Board board = new Board();
        board.loadFromFen(boardState);
        Piece[] pieces = board.boardToArray();

        if(whiteTime != null && whiteTime <= 0) {
            int[] resultArray = blackPlayerInsufficientMaterialToWinChecker(pieces);
            isBlackWinner = resultArray[0] == IS_WINNER;
            if(!isBlackWinner) {
                isDraw = determineIsDraw(resultArray);
                isBlackWinner = !isDraw;
            }
        }
        else if(blackTime != null && blackTime <= 0) {
            int[] resultArray = whitePlayerInsufficientMaterialToWinChecker(pieces);
            isWhiteWinner = resultArray[0] == IS_WINNER;
            if(!isWhiteWinner) {
                isDraw = determineIsDraw(resultArray);
                isWhiteWinner = !isDraw;
            }

        }

        if(isWhiteWinner) {
            gameStatus = GameStatus.WHITE_WINNER;
        } else if(isBlackWinner) {
            gameStatus = GameStatus.BLACK_WINNER;
        } else if(isDraw) {
            gameStatus = GameStatus.DRAW;
        }

        return gameStatus;
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
