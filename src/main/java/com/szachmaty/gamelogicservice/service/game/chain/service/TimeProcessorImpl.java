package com.szachmaty.gamelogicservice.service.game.chain.service;

import com.szachmaty.gamelogicservice.exception.GameException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TimeProcessorImpl implements TimeProcessor {

    private final static String TIME_ERROR = "Error encountered during time calculation!";
    @Override
    public Long countTime(boolean isFirstMove, Long prevSystemTime, Long currSystemTime, Long playerTime) {
        if(isFirstMove) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            return timestamp.getTime();
        }
        Long updatedGameTime = null;
        try {
            float diffBetweenTimeStampsInMiliSec = currSystemTime - prevSystemTime;
            long diffBetweenTimeStampsInSec = Math.round(diffBetweenTimeStampsInMiliSec / 1000);
            updatedGameTime = playerTime - diffBetweenTimeStampsInSec;
            if (updatedGameTime < 0) {
                updatedGameTime = 0l;
            }
        } catch(NullPointerException e) {
            throw new GameException(TIME_ERROR);
        }
        return updatedGameTime;
    }
}
