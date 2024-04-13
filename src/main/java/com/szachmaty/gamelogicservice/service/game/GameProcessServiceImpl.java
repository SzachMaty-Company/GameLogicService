package com.szachmaty.gamelogicservice.service.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.*;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.exception.InvalidMoveException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.external.AIMessageEventData;
import com.szachmaty.gamelogicservice.service.external.UserServiceEventData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameProcessServiceImpl implements GameProcessService {

    private final MoveProcessor moveValidator;
    private final GameOperationService gameOperationService;
    private final TimeProcessor timeProcessor;
    private final GameFinishDetector gameFinishDetector;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final static String GAME_FINISH = "Cannot perform move, game is finished!";

    @Override
    @GameParticipantValidator
    public MoveResponseDTO processGame(GameMessage message) { //TO-DO refactor to ChainOfResponsibilityPattern
        GameDTO gameDTO = gameOperationService.getGameByGameCode(message.getGameCode());
        if(gameDTO == null) {
            throw new GameException("Game with provided code: " + message.getGameCode()  + "doesnt exists!");
        }
        if(determineIfGameIsFinished(gameDTO.getGameStatus())) {
            throw new InvalidMoveException(GAME_FINISH);
        }
        if(GameMode.isAIMode(gameDTO.getGameMode())) {
            MoveResponseDTO responseDTO = processMove(message);
            AIMessageEventData eventData =
                    new AIMessageEventData(this, message.getGameCode(), responseDTO.fen());
            applicationEventPublisher.publishEvent(eventData); //async
            return responseDTO;
        }
        return processMove(message);
    }

    @Override
    public MoveResponseDTO processMove(GameMessage message) {
        GameProcessContext gameProcessContext = new GameProcessContext();
        gameProcessContext.setGameCode(message.getGameCode());
        gameProcessContext.setMove(message.getMove());

        GameDTO gameDTO = gameOperationService.getGameByGameCode(gameProcessContext.getGameCode());
        if(gameDTO == null) {
            throw new GameException("Game with provided code: " + message.getGameCode()  + "doesnt exists!");
        }

        List<String> boards = gameDTO.getFenList();

        if(boards != null) {
            Side side = boards.size() % 2 == 0 ? Side.WHITE : Side.BLACK;
            String currBoardState = boards.isEmpty() ? INIT_CHESS_BOARD : boards.get(boards.size() - 1);
            gameProcessContext.setSide(side);
            gameProcessContext.setCurrBoardState(currBoardState);
        } else { //first move
            gameProcessContext.setFirstMove(true);
            gameProcessContext.setSide(Side.WHITE);
            gameProcessContext.setCurrBoardState(INIT_CHESS_BOARD);
        }

        try {
            boolean isMoveValid = moveValidator.doMove(gameProcessContext);
            if(isMoveValid) {
                String afterMoveBoardState = moveValidator.getBoardState();
                LinkedList<Long> gameHistory = moveValidator.getHistory();
                gameProcessContext.setAfterMoveBoardState(afterMoveBoardState);
                gameProcessContext.setGameHistory(gameHistory);
            } else {
                throw buildInvalidMoveException(gameProcessContext, gameDTO);
            }
        } catch(Exception e) {
            throw buildInvalidMoveException(gameProcessContext, gameDTO);
        }

        if(gameProcessContext.getAfterMoveBoardState() != null) {
            updateTime(gameDTO, gameProcessContext);

            GameStatus boardStateFinish = gameFinishDetector
                    .checkResultBasedOnBoard(gameProcessContext);
            gameProcessContext.setGameStatus(boardStateFinish);
            if(determineIfGameIsFinished(boardStateFinish)) {
                GameDTO game = gameOperationService.updateBoard(gameProcessContext);
                notifyUserDataService(game);
                return gameProcessToMoveResponeConverter(gameProcessContext);
            }

            GameStatus timeFinish = gameFinishDetector
                    .checkResultBasedOnTime(gameProcessContext);
            gameProcessContext.setGameStatus(timeFinish);
            if(determineIfGameIsFinished(timeFinish)) {
                GameDTO game = gameOperationService.updateBoard(gameProcessContext);
                notifyUserDataService(game);
                return gameProcessToMoveResponeConverter(gameProcessContext);
            }

            gameOperationService.updateBoard(gameProcessContext);
            return gameProcessToMoveResponeConverter(gameProcessContext);
        } else {
            throw buildInvalidMoveException(gameProcessContext, gameDTO);
        }
    }

    private boolean determineIfGameIsFinished(GameStatus gameStatus) {
        return gameStatus == GameStatus.WHITE_WINNER ||
                gameStatus == GameStatus.BLACK_WINNER ||
                gameStatus == GameStatus.DRAW;
    }

    private MoveResponseDTO gameProcessToMoveResponeConverter(GameProcessContext gameProcessContext) {
        Long time;
        Side nextMove;
        if(gameProcessContext.getSide() == Side.WHITE) {
            time = gameProcessContext.getWhiteTime();
            nextMove = Side.BLACK;
        } else {
            time = gameProcessContext.getBlackTime();
            nextMove = Side.WHITE;
        }
        return new MoveResponseDTO(gameProcessContext.getMove(),
                gameProcessContext.getAfterMoveBoardState(), time, nextMove.toString(), gameProcessContext.getGameStatus());
    }

    private void updateTime(GameDTO gameDTO, GameProcessContext gameProcessContext) {
        if(gameProcessContext.isFirstMove()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            gameProcessContext.setPrevSystemTime(timestamp.getTime());
            gameProcessContext.setWhiteTime(gameDTO.getWhiteTime());
            gameProcessContext.setBlackTime(gameDTO.getBlackTime());
            return;
        }
        Side side = gameProcessContext.getSide();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currTime = timestamp.getTime();

        if(side == Side.WHITE) {
            Long whiteTime = timeProcessor.countTime(gameProcessContext.isFirstMove(), gameDTO.getPrevSystemTime(),
                            currTime,  gameDTO.getWhiteTime());
            gameProcessContext.setWhiteTime(whiteTime);
        } else {
            Long blackTime = timeProcessor.countTime(gameProcessContext.isFirstMove(), gameDTO.getPrevSystemTime(),
                            currTime, gameDTO.getBlackTime());
            gameProcessContext.setBlackTime(blackTime);
        }
        gameProcessContext.setPrevSystemTime(currTime);
    }

    private InvalidMoveException buildInvalidMoveException(GameProcessContext gameProcessContext, GameDTO gameDTO) {
        return new InvalidMoveException("Move: " + gameProcessContext.getMove() + " is invalid!",
                gameProcessContext.getMove(),
                gameProcessContext.getCurrBoardState(),
                gameProcessContext.getSide() == Side.WHITE ? gameDTO.getWhiteTime() : gameDTO.getBlackTime(),
                gameProcessContext.getGameCode()
        );
    }

    private void notifyUserDataService(GameDTO game) {
        UserServiceEventData eventData = new UserServiceEventData(this, game);
        applicationEventPublisher.publishEvent(eventData);
        System.out.println("Wyslaned do klienta " + game.toString());
    }
}
