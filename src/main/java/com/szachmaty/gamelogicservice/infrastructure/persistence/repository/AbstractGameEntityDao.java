package com.szachmaty.gamelogicservice.infrastructure.persistence.repository;

import com.szachmaty.gamelogicservice.infrastructure.persistence.entity.GameEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractGameEntityDao implements GameEntityDao, QueryByExampleExecutor<GameEntity> {


    @Override
    public abstract GameEntity findGameById(long gameId);

    @Override
    public abstract List<GameEntity> findAllGames();

    @Override
    public abstract GameEntity saveGame(GameEntity game);

    @Override
    public abstract GameEntity updateGame(GameEntity game);


    @Override
    public abstract GameEntity deleteGame(GameEntity game);

    @Override
    public abstract void deleteGameById(long gameId);

    @Override
    public abstract <S extends GameEntity> Optional<S> findOne(Example<S> example);

    @Override
    public abstract <S extends GameEntity> Iterable<S> findAll(Example<S> example);

    @Override
    public abstract <S extends GameEntity> Iterable<S> findAll(Example<S> example, Sort sort);

    @Override
    public abstract <S extends GameEntity> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    public abstract <S extends GameEntity> long count(Example<S> example);

    @Override
    public abstract <S extends GameEntity> boolean exists(Example<S> example);

    @Override
    public abstract <S extends GameEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
