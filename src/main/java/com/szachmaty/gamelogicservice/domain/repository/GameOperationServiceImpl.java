package com.szachmaty.gamelogicservice.domain.repository;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.application.game.GameProcessDTO;
import com.szachmaty.gamelogicservice.application.manager.GameOperationService;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.GameStatus;
import com.szachmaty.gamelogicservice.domain.mapper.Mapper;
import com.szachmaty.gamelogicservice.domain.repository.exception.GameEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Component
@RequiredArgsConstructor
public class GameOperationServiceImpl implements GameOperationService {

    private final GameEntityRepository gameEntityRepository;
    private final Mapper mapperProvider;

    @Override
    public List<GameDTO> getAll() {
        Iterable<GameEntity> games = gameEntityRepository.findAll();
        List<GameEntity> gameEntities = StreamSupport.stream(games.spliterator(), false).toList();
        if(gameEntities.isEmpty()) {
            return null;
        }
        return gameEntities.stream()
                .map(game -> mapperProvider.modelMapper().map(game, GameDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean validatePlayerTurn(String gameCode, String principal) {
        GameEntity gameEntity = gameEntityRepository.findByGameCode(gameCode);
        if(gameEntity == null) {
            return false;
        }
        boolean isWhiteUser = principal.equals(gameEntity.getWhiteUserId());
        Side side;
        if(gameEntity.getMoveList() == null) {
            return isWhiteUser; //case when its first move
        }

        if(gameEntity.getMoveList().size() % 2 == 0) {
            side = Side.WHITE;
        } else {
            side = Side.BLACK;
        }

        if(isWhiteUser && side.equals(Side.WHITE)) {
            return true;
        } else {
            return !isWhiteUser && side.equals(Side.BLACK);
        }
    }

    @Override
    public GameDTO getBoards(String gameCode) {
        GameEntity game = gameEntityRepository.findByGameCode(gameCode);
        GameDTO gameDTO = mapperProvider.modelMapper().map(game, GameDTO.class);
        if(gameDTO != null) {
            return gameDTO;
        } else {
            throw new GameEntityNotFoundException("Game with given id: " + gameCode + " doesn't exists!");
        }
    }

    @Override
    public GameDTO updateBoard(GameProcessDTO gameProcessDTO) {
        String gameCode = gameProcessDTO.getGameCode();
        String move = gameProcessDTO.getMove();
        String boardState = gameProcessDTO.getAfterMoveBoardState();
        Side side = gameProcessDTO.getSide();
        LinkedList<Long> gameHistory = gameProcessDTO.getGameHistory();
        boolean isFinished = gameProcessDTO.isFinished();
        boolean isFirstMove = gameProcessDTO.isFirstMove();

        GameEntity game = gameEntityRepository.findByGameCode(gameCode);
        if(game != null) {
            List<String> moveList = game.getMoveList();
            if(moveList != null) {
                moveList.add(move);
                game.setMoveList(moveList);
            } else {
                List<String> moves = new ArrayList<>();
                moves.add(move);
                game.setMoveList(moves);
            }
            List<String> boardStates = game.getBoardStateList();
            if(boardStates != null) {
                boardStates.add(boardState);
            } else {
                List<String> boards = new ArrayList<>();
                boards.add(boardState);
                game.setBoardStateList(boards);
            }
            game.setGameHistory(gameHistory);
            if(isFirstMove) {
                game.setGameStatus(GameStatus.IN_GAME);
            }
            if(isFinished) {
                game.setGameStatus(GameStatus.FINISHED);
            }
            if(side == Side.WHITE) {
                game.setWhiteTime(gameProcessDTO.getWhiteTime());
            }
            else {
                game.setBlackTime(gameProcessDTO.getBlackTime());
            }
            game.setPrevMoveTime(gameProcessDTO.getPrevMoveTime());
            GameEntity gameEntity = gameEntityRepository.save(game);
            return mapperProvider.modelMapper().map(gameEntity, GameDTO.class);
        } else {
            throw new GameEntityNotFoundException("Cannot find game with gameCode " + gameCode);
        }
    }

    @Override
    public void saveNewGame(GameDTO gameDTO) {
        GameEntity gameEntity = mapperProvider.modelMapper().map(gameDTO, GameEntity.class);
        gameEntityRepository.save(gameEntity);
    }

    @Override
    public void deleteGameByGameCode(String gameCode) {
        if(gameCode != null && !gameCode.isEmpty()) {
            gameEntityRepository.deleteByGameCode(gameCode);
        }
    }

    @Override
    public boolean isPlayerGameParticipant(String gameCode, String userId) {
        GameEntity gameEntity = gameEntityRepository.findByGameCode(gameCode);
        return gameEntity != null  && (userId.equals(gameEntity.getWhiteUserId()) ||
                        userId.equals(gameEntity.getBlackUserId()));
    }

}
