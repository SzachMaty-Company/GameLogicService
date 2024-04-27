package com.szachmaty.gamelogicservice.repository;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.config.mapper.Mapper;
import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.data.entity.GameEntity;
import com.szachmaty.gamelogicservice.exception.GameEntityNotFoundException;
import com.szachmaty.gamelogicservice.exception.GameFinishException;
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
    private final static String GAME_NOT_EXISTS = "Game was finished or never existed!";

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
    public GameDTO getGameByGameCode(String gameCode) {
        GameEntity game = gameEntityRepository.findByGameCode(gameCode);
        if(game == null) {
            return null;
        }
        return mapperProvider.modelMapper().map(game, GameDTO.class);
    }

    @Override
    public GameDTO updateBoard(GameProcessContext gameProcessContext) {
        String gameCode = gameProcessContext.getGameCode();
        String move = gameProcessContext.getMove();
        String boardState = gameProcessContext.getNextFen();
        Side side = gameProcessContext.getSide();
        LinkedList<Long> gameHistory = gameProcessContext.getGameHistory();
        boolean isFirstMove = gameProcessContext.isFirstMove();

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
            List<String> boardStates = game.getFenList();
            if(boardStates != null) {
                boardStates.add(boardState);
            } else {
                List<String> boards = new ArrayList<>();
                boards.add(boardState);
                game.setFenList(boards);
            }
            game.setGameHistory(gameHistory);
            game.setGameStatus(gameProcessContext.getGameStatus());

            if(isFirstMove) {
                game.setGameStatus(gameProcessContext.getGameStatus());
            }
            if(side == Side.WHITE) {
                game.setWhiteTime(gameProcessContext.getWhiteTime());
                game.setSideToMove(Side.BLACK);
            }
            else {
                game.setBlackTime(gameProcessContext.getBlackTime());
                game.setSideToMove(Side.WHITE);
            }
            game.setPrevSystemTime(gameProcessContext.getPrevSystemTime());
            GameEntity gameEntity = gameEntityRepository.save(game);
            return mapperProvider.modelMapper().map(gameEntity, GameDTO.class);
        } else {
            throw new GameEntityNotFoundException(String.format("Cannot find game with gameCode: %s", gameCode));
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
            GameEntity game = gameEntityRepository.findByGameCode(gameCode);
            gameEntityRepository.delete(game); //spring-data-redis doesn't support deleteBy
        }
    }

    @Override
    public boolean isPlayerGameParticipant(String gameCode, String userId) {
        GameEntity gameEntity = gameEntityRepository.findByGameCode(gameCode);
        if(gameEntity == null) {
            throw new GameFinishException(GAME_NOT_EXISTS);
        }
        return userId.equals(gameEntity.getWhiteUserId()) ||
                        userId.equals(gameEntity.getBlackUserId());
    }

}
