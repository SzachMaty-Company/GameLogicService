package com.szachmaty.gamelogicservice.infrastructure.persistence.repository;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.szachmaty.gamelogicservice.infrastructure.persistence.constants.EntityConstans.USER_HASH;

@Repository
public class GameEntityDaoImpl implements GameEntityDao {
    @Autowired
//    @Qualifier("stringRedisTemplate")
    public RedisTemplate<String,Object> template;


//    public GameEntityDaoImpl(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }

    @Override
    public GameEntity findGameById(long gameId) {
//        Object retrievedObject = redisTemplate.opsForHash().get(USER_HASH, gameId);
//        if(retrievedObject != null && retrievedObject instanceof GameEntity) {
//            return (GameEntity) retrievedObject;
//        }
        return null;
    }

    @Override
    public List<GameEntity> findAllGames() {
        return null;
    }

    @Override
    public GameEntity saveGame(GameEntity game) {
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        template.opsForHash().put(USER_HASH,game.getGameId(),game);
        return game;
    }

    @Override
    public GameEntity updateGame(GameEntity game) {
        return null;
    }

    @Override
    public GameEntity deleteGame(GameEntity game) {
        return null;
    }

    @Override
    public void deleteGameById(long gameId) {

    }
}
