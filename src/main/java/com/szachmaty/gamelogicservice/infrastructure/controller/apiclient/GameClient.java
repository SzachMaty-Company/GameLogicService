package com.szachmaty.gamelogicservice.infrastructure.controller.apiclient;

import com.szachmaty.gamelogicservice.domain.dto.GameDTO;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.CheckPlayerResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import static com.szachmaty.gamelogicservice.infrastructure.controller.constant.APIRoutes.GAME_CHECK_PLAYER;
import static com.szachmaty.gamelogicservice.infrastructure.controller.constant.APIRoutes.GAME_FINISH;


@FeignClient(url = "${external.service.user-service.url}", name = "GAME-CLIENT", configuration = FeignGameClientConfiguration.class)
public interface GameClient {
    @GetMapping("/entries")
    List<Object> getTestEntries();
    @PostMapping(path = GAME_FINISH)
    void sendGameAfterFinish(GameDTO game);
}
