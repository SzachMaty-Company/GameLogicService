package com.szachmaty.gamelogicservice.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("move")
@TypeAlias("MoveEntity.class")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoveEntity implements Serializable {
    @Id
    private long moveId;
    private String move;
}