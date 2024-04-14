package com.szachmaty.gamelogicservice.service.game.chain.handler;

import com.github.bhlangonijr.chesslib.Side;
import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;
import com.szachmaty.gamelogicservice.service.game.chain.service.TimeProcessor;
import com.szachmaty.gamelogicservice.service.game.chain.GameProcessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class TimeUpdater implements GameProcessHandler {

    private final TimeProcessor timeProcessor;

    @Override
    public boolean process(GameProcessContext context) {
        if(context.isFirstMove()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            context.setPrevSystemTime(timestamp.getTime());
            context.setWhiteTime(context.getWhiteTime());
            context.setBlackTime(context.getBlackTime());
            return true;
        }
        Side side = context.getSide();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long currTime = timestamp.getTime();

        if(side == Side.WHITE) {
            Long whiteTime = timeProcessor.countTime(context.isFirstMove(), context.getPrevSystemTime(),
                    currTime, context.getWhiteTime());
            context.setWhiteTime(whiteTime);
        } else {
            Long blackTime = timeProcessor.countTime(context.isFirstMove(), context.getPrevSystemTime(),
                    currTime, context.getBlackTime());
            context.setBlackTime(blackTime);
        }

        context.setPrevSystemTime(currTime);

        return true;
    }
}
