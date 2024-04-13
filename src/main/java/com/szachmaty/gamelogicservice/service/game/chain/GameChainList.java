package com.szachmaty.gamelogicservice.service.game.chain;

import com.szachmaty.gamelogicservice.data.dto.GameProcessContext;

import java.util.ArrayList;
import java.util.List;

public class GameChainList {

    private final List<GameProcessHandler> items;

    public GameChainList() {
        items = new ArrayList<>();
    }

    public void processChain(GameProcessContext context) {
        for (GameProcessHandler item : items) {
            if(!item.process(context)) {
                break;
            }
        }
    }



    public GameChainList addNext(GameProcessHandler item) {
        items.add(item);

        return this;
    }

}
