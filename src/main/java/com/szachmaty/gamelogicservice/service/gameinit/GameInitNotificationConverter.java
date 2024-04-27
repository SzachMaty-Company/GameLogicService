package com.szachmaty.gamelogicservice.service.gameinit;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;

public interface GameInitNotificationConverter {
    GameInitNotification toGameInitNotification(GameDTO gameDTO);
}
