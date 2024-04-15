package com.szachmaty.gamelogicservice.service.gameinit;

import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;

public interface ChatServiceCallPreparator {
    void notifyChatService(GameInitNotification notification);
}
