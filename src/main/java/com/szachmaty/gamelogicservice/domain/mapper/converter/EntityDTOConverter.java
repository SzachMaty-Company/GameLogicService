package com.szachmaty.gamelogicservice.domain.mapper.converter;

import com.github.bhlangonijr.chesslib.move.Move;
import com.szachmaty.gamelogicservice.application.manager.GameDTOManager;
import com.szachmaty.gamelogicservice.domain.dto.GameBPlDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.domain.dto.GameWPlDTO;
import com.szachmaty.gamelogicservice.domain.entity.GameEntity;
import com.szachmaty.gamelogicservice.domain.entity.MoveEntity;
import com.szachmaty.gamelogicservice.domain.mapper.Mapper;
import com.szachmaty.gamelogicservice.domain.repository.GameEntityDao;
import com.szachmaty.gamelogicservice.domain.repository.exception.GameDTOEntityConversionException;
import com.szachmaty.gamelogicservice.domain.repository.exception.GameEntityDTOConversionException;
import com.szachmaty.gamelogicservice.domain.repository.exception.GameEntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EntityDTOConverter implements GameDTOManager {

    private final GameEntityDao gameEntityDao;
    private final Mapper mapperProvider;
    private final static String INIT_CHESS_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";  //TO BE MOVED

    @Autowired
    public EntityDTOConverter(GameEntityDao gameEntityDao, Mapper mapperProvider) {
        this.gameEntityDao = gameEntityDao;
        this.mapperProvider = mapperProvider;
    }

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
    public String getCurrBoardStateByGameCode(String gameCode) {
        GameEntity game = gameEntityDao.findGameByGameCode(gameCode);
        if(game != null) {
            List<String> boardList = game.getBoardStateList();
            return boardList != null && boardList.size() != 0 ? boardList.get(boardList.size() - 1) : INIT_CHESS_BOARD;
        } else {
            return "";
        }
    }

    @Override
    public void updateBoard(String move, String boardState, String gameCode) {
        GameEntity game = gameEntityDao.findGameByGameCode(gameCode);
        if(game != null) {
            List<MoveEntity> moveList = game.getMoveList();
            if(moveList != null) {
                MoveEntity moveEntity = new MoveEntity();
                moveEntity.setMove(move);
                moveList.add(moveEntity);
            }
            List<String> boardStates = game.getBoardStateList();
            if(boardStates != null) {
                boardStates.add(boardState);
            }
            gameEntityDao.updateGame(game);
        } else {
            throw new GameEntityNotFoundException(""); //TO BE CHANGED
        }
    }

    @Override
    public void saveNewGame(GameDTO gameDTO) {
        GameEntity gameEntity = mapperProvider.modelMapper().map(gameDTO, GameEntity.class);
        System.out.println(gameEntity);
        gameEntityDao.saveGame(gameEntity);
    }

    @Override
    public void deleteGame(GameDTO gameDTO) {
        GameEntity gameEntity = mapperProvider.modelMapper().map(gameDTO, GameEntity.class);
        System.out.println(gameEntity);
        gameEntityDao.deleteGame(gameEntity);
    }

}
