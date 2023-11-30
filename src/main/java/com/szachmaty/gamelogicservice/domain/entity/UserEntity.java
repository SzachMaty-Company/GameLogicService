package com.szachmaty.gamelogicservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

import static com.szachmaty.gamelogicservice.domain.constants.EntityConstants.USER_HASH;

@RedisHash(USER_HASH)
@TypeAlias("UserEntity.class")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements Serializable {
    @Id
    private long userId;
    private String username;
}
