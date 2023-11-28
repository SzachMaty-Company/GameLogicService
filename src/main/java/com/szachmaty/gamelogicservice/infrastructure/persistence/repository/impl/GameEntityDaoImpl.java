package com.szachmaty.gamelogicservice.infrastructure.persistence.repository.impl;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import com.szachmaty.gamelogicservice.infrastructure.persistence.repository.AbstractGameEntityDao;
import com.szachmaty.gamelogicservice.infrastructure.persistence.repository.GameEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.szachmaty.gamelogicservice.infrastructure.persistence.constants.EntityConstants.GAME_HASH;

@Repository
public class GameEntityDaoImpl extends AbstractGameEntityDao {

    public RedisTemplate<String,Object> template;

    @Autowired
    public GameEntityDaoImpl(RedisTemplate redisTemplate) {
        this.template = redisTemplate;
    }

    @Override
    public GameEntity findGameById(long gameId) {
        Object retrievedObject = template.opsForHash().get(GAME_HASH, gameId);
        if(retrievedObject instanceof GameEntity) {
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
    public <S extends GameEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends GameEntity> Iterable<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends GameEntity> Iterable<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends GameEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends GameEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends GameEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends GameEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public GameEntity deleteGame(GameEntity game) {
        return null;
    }

}
