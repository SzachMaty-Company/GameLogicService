package com.szachmaty.gamelogicservice.infrastructure.persistence.repository.impl;

import com.szachmaty.gamelogicservice.domain.game.Game;
import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.repository.GameEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.szachmaty.gamelogicservice.infrastructure.persistence.constants.EntityConstans.GAME_HASH;
import static com.szachmaty.gamelogicservice.infrastructure.persistence.constants.EntityConstans.USER_HASH;

@Repository
public class GameEntityDaoImpl implements GameEntityDao {

    public RedisTemplate<String,Object> template;

    @Autowired
    public GameEntityDaoImpl(RedisTemplate redisTemplate) {
        this.template = redisTemplate;
    }

    @Override
    public GameEntity findGameById(long gameId) {
        Object retrievedObject = template.opsForHash().get(GAME_HASH, gameId);
        if(retrievedObject != null && retrievedObject instanceof GameEntity) {
            return (GameEntity) retrievedObject;
        }
        return null;
    }

    @Override
    public List<GameEntity> findAllGames() {
        List<Object> objectList = template.opsForHash().values(GAME_HASH);
        List<GameEntity> gameEntityList = new ArrayList<>();
        for(var o : objectList) {
            if(o instanceof GameEntity) {
                gameEntityList.add((GameEntity) o);
            }
        }
        return gameEntityList;
    }

    @Override
    public GameEntity saveGame(GameEntity game) {
        template.opsForHash().put(GAME_HASH,game.getGameId(),game);
        return game;
    }

    @Override
    public GameEntity updateGame(GameEntity game) {
        template.opsForHash().put(GAME_HASH,game.getGameId(), game);
        return game;
    }
    @Override
    public void deleteGameById(long gameId) {
        template.opsForHash().delete(GAME_HASH, gameId);
    }
    @Override
    public GameEntity deleteGame(GameEntity game) {
        return null;
    }

}
