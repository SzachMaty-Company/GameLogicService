package com.szachmaty.gamelogicservice.application.repository;

import com.szachmaty.gamelogicservice.domain.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserEntityRepository extends CrudRepository<UserEntity, String> {
}
