package com.szachmaty.gamelogicservice.domain.mapper.converter;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.entity.GameStatus;
import com.szachmaty.gamelogicservice.domain.mapper.Mapper;
import com.szachmaty.gamelogicservice.domain.repository.GameEntityRepository;
import com.szachmaty.gamelogicservice.domain.repository.exception.GameEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class EntityDTOConverter implements GameDTOManager {

    private final GameEntityRepository gameEntityRepository;
    private final Mapper mapperProvider;

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
    public UserDTO getUserById(String uuid) {
        GameEntity whiteGameEntity = gameEntityRepository.findByWhiteUserUuid(uuid);
        if(whiteGameEntity == null) {
            GameEntity blackGameEnttiy = gameEntityRepository.findByBlackUserUuid(uuid);
//            return
        }
        return new UserDTO();
//        return gameEntityRepository.findById(userId);
    }

    @Override
    public GameDTO updateBoard(String move, String boardState, String gameCode, LinkedList<Long> gameHistory, boolean isFinished) {
        GameEntity game = gameEntityRepository.findByGameCode(gameCode);
        if(game != null) {
            List<MoveEntity> moveList = game.getMoveList();
            if(moveList != null) {
                MoveEntity moveEntity = new MoveEntity();
                moveEntity.setMove(move);
                moveList.add(moveEntity);
            } else {
                List<MoveEntity> moves = new ArrayList<>();
                MoveEntity moveEntity = new MoveEntity();
                moves.add(moveEntity);
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
            if(isFinished) {
                game.setGameStatus(GameStatus.FINISHED);
            }
            GameEntity gameEntity = gameEntityRepository.save(game);
            return mapperProvider.modelMapper().map(gameEntity, GameDTO.class);
        } else {
            throw new GameEntityNotFoundException("Cannot find game with gameCode " + gameCode);
        }
    }

    @Override
    public void saveNewGame(GameDTO gameDTO) {
        GameEntity gameEntity = mapperProvider.modelMapper().map(gameDTO, GameEntity.class);
        gameEntity.setGameId("0");
        System.out.println(gameEntity);
        gameEntityRepository.save(gameEntity);
    }

    @Override
    public void deleteGame(GameDTO gameDTO) {
        GameEntity gameEntity = mapperProvider.modelMapper().map(gameDTO, GameEntity.class);
        System.out.println(gameEntity);
        gameEntityRepository.delete(gameEntity);
    }

    @Override
    public boolean isPlayerGameParticipant(String gameCode, String user) {
        GameEntity gameEntity = gameEntityRepository.findByGameCode(gameCode);
        return gameEntity != null  && (user.equals(gameEntity.getWhiteUserId()) ||
                        user.equals(gameEntity.getBlackUserId()));
    }

}
