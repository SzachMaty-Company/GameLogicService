package com.szachmaty.gamelogicservice.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

import static com.szachmaty.gamelogicservice.infrastructure.persistence.constants.EntityConstants.MOVE_HASH;


@RedisHash(MOVE_HASH)
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
