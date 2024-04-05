package com.szachmaty.gamelogicservice.service.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameMessage;
import com.szachmaty.gamelogicservice.data.dto.GameProcessDTO;
import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;
import com.szachmaty.gamelogicservice.data.entity.GameStatus;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.exception.InvalidMoveException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.service.game.external.AIMessageEventData;
import com.szachmaty.gamelogicservice.service.game.external.UserServiceEventData;
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
    public MoveResponseDTO processGame(GameMessage message) {
        GameDTO gameDTO = gameOperationService.getGameByGameCode(message.getGameCode());
        if(gameDTO == null) {
            throw new GameException("Game with provided code: " + message.getGameCode()  + "doesnt exists!");
        }
        if(determineIfGameIsFinished(gameDTO.getGameStatus())) {
            throw new InvalidMoveException(GAME_FINISH);
        }
        if(gameDTO.isGameWithAI()) {
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
        GameProcessDTO gameProcessDTO = new GameProcessDTO();
        gameProcessDTO.setGameCode(message.getGameCode());
        gameProcessDTO.setMove(message.getMove());

        GameDTO gameDTO = gameOperationService.getGameByGameCode(gameProcessDTO.getGameCode());
        if(gameDTO == null) {
            throw new GameException("Game with provided code: " + message.getGameCode()  + "doesnt exists!");
        }

        List<String> boards = gameDTO.getBoardStateList();

        if(boards != null) {
            Side side = boards.size() % 2 == 0 ? Side.WHITE : Side.BLACK;
            String currBoardState = boards.size() == 0 ? INIT_CHESS_BOARD : boards.get(boards.size() - 1);
            gameProcessDTO.setSide(side);
            gameProcessDTO.setCurrBoardState(currBoardState);
        } else { //first move
            gameProcessDTO.setFirstMove(true);
            gameProcessDTO.setSide(Side.WHITE);
            gameProcessDTO.setCurrBoardState(INIT_CHESS_BOARD);
        }

        try {
            boolean isMoveValid = moveValidator.doMove(gameProcessDTO);
            if(isMoveValid) {
                String afterMoveBoardState = moveValidator.getBoardState();
                LinkedList<Long> gameHistory = moveValidator.getHistory();
                gameProcessDTO.setAfterMoveBoardState(afterMoveBoardState);
                gameProcessDTO.setGameHistory(gameHistory);
            } else {
                throw buildInvalidMoveException(gameProcessDTO, gameDTO);
            }
        } catch(Exception e) {
            throw buildInvalidMoveException(gameProcessDTO, gameDTO);
        }

        if(gameProcessDTO.getAfterMoveBoardState() != null) {
            updateTime(gameDTO, gameProcessDTO);

            GameStatus boardStateFinish = gameFinishDetector
                    .checkResultBasedOnBoard(gameProcessDTO);
            gameProcessDTO.setGameStatus(boardStateFinish);
            if(determineIfGameIsFinished(boardStateFinish)) {
                GameDTO game = gameOperationService.updateBoard(gameProcessDTO);
                notifyUserDataService(game);
                return gameProcessToMoveResponeConverter(gameProcessDTO);
            }

            GameStatus timeFinish = gameFinishDetector
                    .checkResultBasedOnTime(gameProcessDTO);
            gameProcessDTO.setGameStatus(timeFinish);
            if(determineIfGameIsFinished(timeFinish)) {
                GameDTO game = gameOperationService.updateBoard(gameProcessDTO);
                notifyUserDataService(game);
                return gameProcessToMoveResponeConverter(gameProcessDTO);
            }

            gameOperationService.updateBoard(gameProcessDTO);
            return gameProcessToMoveResponeConverter(gameProcessDTO);
        } else {
            throw buildInvalidMoveException(gameProcessDTO, gameDTO);
        }
    }

    private boolean determineIfGameIsFinished(GameStatus gameStatus) {
        return gameStatus == GameStatus.WHITE_WINNER ||
                gameStatus == GameStatus.BLACK_WINNER ||
                gameStatus == GameStatus.DRAW;
    }

    private MoveResponseDTO gameProcessToMoveResponeConverter(GameProcessDTO gameProcessDTO) {
        Long time;
        Side nextMove;
        if(gameProcessDTO.getSide() == Side.WHITE) {
            time = gameProcessDTO.getWhiteTime();
            nextMove = Side.BLACK;
        } else {
            time = gameProcessDTO.getBlackTime();
            nextMove = Side.WHITE;
        }
        return new MoveResponseDTO(gameProcessDTO.getMove(),
                gameProcessDTO.getAfterMoveBoardState(), time, nextMove.toString(), gameProcessDTO.getGameStatus());
    }

    private void updateTime(GameDTO gameDTO, GameProcessDTO gameProcessDTO) {
        if(gameProcessDTO.isFirstMove()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            gameProcessDTO.setPrevSystemTime(timestamp.getTime());
            gameProcessDTO.setWhiteTime(gameDTO.getWhiteTime());
            gameProcessDTO.setBlackTime(gameDTO.getBlackTime());
            return;
        }
        Side side = gameProcessDTO.getSide();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currTime = timestamp.getTime();

        if(side == Side.WHITE) {
            Long whiteTime = timeProcessor.countTime(gameProcessDTO.isFirstMove(), gameDTO.getPrevSystemTime(),
                            currTime,  gameDTO.getWhiteTime());
            gameProcessDTO.setWhiteTime(whiteTime);
        } else {
            Long blackTime = timeProcessor.countTime(gameProcessDTO.isFirstMove(), gameDTO.getPrevSystemTime(),
                            currTime, gameDTO.getBlackTime());
            gameProcessDTO.setBlackTime(blackTime);
        }
        gameProcessDTO.setPrevSystemTime(currTime);
    }

    private InvalidMoveException buildInvalidMoveException(GameProcessDTO gameProcessDTO, GameDTO gameDTO) {
        return new InvalidMoveException("Move: " + gameProcessDTO.getMove() + " is invalid!",
                gameProcessDTO.getMove(),
                gameProcessDTO.getCurrBoardState(),
                gameProcessDTO.getSide() == Side.WHITE ? gameDTO.getWhiteTime() : gameDTO.getBlackTime(),
                gameProcessDTO.getGameCode()
        );
    }

    private void notifyUserDataService(GameDTO game) {
        UserServiceEventData eventData = new UserServiceEventData(this, game);
        applicationEventPublisher.publishEvent(eventData);
        System.out.println("Wyslaned do klienta " + game.toString());
    }
}
