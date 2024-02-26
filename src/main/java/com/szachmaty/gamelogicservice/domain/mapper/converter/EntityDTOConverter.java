package com.szachmaty.gamelogicservice.domain.mapper.converter;

import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.application.repository.UserEntityRepository;
import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.UserDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.entity.enumeration.GameStatus;
import com.szachmaty.gamelogicservice.domain.mapper.Mapper;
import com.szachmaty.gamelogicservice.application.repository.GameEntityDao;
import com.szachmaty.gamelogicservice.application.repository.GameEntityRepository;
import com.szachmaty.gamelogicservice.application.repository.exception.GameDTOEntityConversionException;
import com.szachmaty.gamelogicservice.application.repository.exception.GameEntityDTOConversionException;
import com.szachmaty.gamelogicservice.application.repository.exception.GameEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class EntityDTOConverter implements GameDTOManager {

    private final GameEntityDao gameEntityDao;
    private final GameEntityRepository gameEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final Mapper mapperProvider;

    @Override
    public GameWPlDTO getGameStateWPlById(long gameId) {
        GameEntity game = gameEntityDao.findGameById(gameId);
        if(game == null) {
            throw new GameEntityNotFoundException("Cannot find GameEntity with given id:" + gameId);
        }
        ModelMapper modelMapper = mapperProvider.gameEntityDTOMapper(GameWPlDTO.class);
        GameWPlDTO gameWPlDTO = modelMapper.map(game, GameWPlDTO.class);
        if(gameWPlDTO == null) {
            throw new GameEntityDTOConversionException("Cannot convert: " + GameEntity.class + "to " + GameWPlDTO.class);
        }
        return gameWPlDTO;
    }

    @Override
    public GameBPlDTO getGameStateBPlById(long gameId) {
        GameEntity game = gameEntityDao.findGameById(gameId);
        if(game == null) {
            throw new GameEntityNotFoundException("Cannot find GameEntity with given id:" + gameId);
        }
        ModelMapper modelMapper = mapperProvider.gameEntityDTOMapper(GameBPlDTO.class);
        GameBPlDTO gameBPlDTO = modelMapper.map(game, GameBPlDTO.class);
        if(gameBPlDTO == null) {
            throw new GameEntityDTOConversionException("Cannot convert: " +
                    GameEntity.class + "to " + GameBPlDTO.class);
        }
        return gameBPlDTO;
    }

    @Override
    public void saveGameStateWPl(GameWPlDTO gameWPlDTO) {
         ModelMapper modelMapper = mapperProvider.gameDTOEntityMapper(GameWPlDTO.class);
         GameEntity gameEntity = modelMapper.map(gameWPlDTO, GameEntity.class);
         if(gameEntity == null) {
             throw new GameDTOEntityConversionException("Cannot convert: " +
                     GameWPlDTO.class + "to " + GameEntity.class);
         }
         gameEntityDao.updateGame(gameEntity);
    }

    @Override
    public void saveGameStateBPl(GameBPlDTO gameBPlDTO) {
        ModelMapper modelMapper = mapperProvider.gameDTOEntityMapper(GameBPlDTO.class);
        GameEntity gameEntity = modelMapper.map(gameBPlDTO, GameEntity.class);
        if(gameEntity == null) {
            throw new GameDTOEntityConversionException("Cannot convert: " +
                    GameBPlDTO.class + "to " + GameEntity.class);
        }
        gameEntityDao.updateGame(gameEntity);
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
    public UserDTO getUserById(String userId) {
        return userEntityRepository.findById(userId);
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
    public boolean isPlayerGameParticipant(String gameCode, String userId) {
        return true;
    }

}
