package com.szachmaty.gamelogicservice.usecase.service;

import com.szachmaty.gamelogicservice.infrastructure.persistence.repository.GameEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    public GameEntityRepository gameEntityRepository;


}
