package com.szachmaty.gamelogicservice.service.game;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameProcessDTO;
import com.szachmaty.gamelogicservice.exception.GameException;
import com.szachmaty.gamelogicservice.exception.InvalidMoveException;
import com.szachmaty.gamelogicservice.repository.GameOperationService;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.controller.apiclient.GameClient;
import com.szachmaty.gamelogicservice.data.dto.GameFinishDTO;
import com.szachmaty.gamelogicservice.data.dto.MoveResponseDTO;
import com.szachmaty.gamelogicservice.data.dto.GameMessage;
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
    private final GameClient gameClient;
    private final TimeProcessor timeProcessor;
    private final GameFinishDetector gameFinishDetector;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Override
    @GameParticipantValidator
    public MoveResponseDTO processGame(GameMessage message) {
        GameDTO gameDTO = gameOperationService.getGameByGameCode(message.getGameCode());
        if(gameDTO == null) {
            throw new GameException("Game with provided code: " + message.getGameCode()  + "doesnt exists!");
        }
        if(gameDTO.isGameWithAI()) {
            MoveResponseDTO responseDTO = processMove(message);
            GameAIMessageEventData eventData =
                    new GameAIMessageEventData(this, message.getGameCode(), responseDTO.fen());
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
            GameFinishDTO boardStateFinish = gameFinishDetector
                    .checkResultBasedOnBoard(gameProcessDTO);
            GameFinishDTO timeFinish = gameFinishDetector
                    .checkResultBasedOnTime(gameProcessDTO);

            if((boardStateFinish != null && boardStateFinish.isFinish()) ||
                    timeFinish != null && timeFinish.isFinish() ) {
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
                gameProcessDTO.getAfterMoveBoardState(), time, nextMove.toString());
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
        System.out.println("Wyslaned do klienta " + game.toString());
//        gameClient.sendGameAfterFinish(game);
        gameOperationService.deleteGameByGameCode(game.getGameCode());
    }
}
