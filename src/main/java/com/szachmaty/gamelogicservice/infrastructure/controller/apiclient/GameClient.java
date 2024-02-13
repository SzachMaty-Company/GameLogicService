package com.szachmaty.gamelogicservice.infrastructure.controller.apiclient;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.CheckPlayerResp;
import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameCheckPlayerReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import static com.szachmaty.gamelogicservice.infrastructure.controller.constant.APIRoutes.GAME_CHECK_PLAYER;


@FeignClient(url = "https://api.publicapis.org", name = "GAME-CLIENT", configuration = FeignGameClientConfiguration.class)
public interface GameClient {
    @GetMapping("/entries")
    List<Object> getTestEntries();
    @PostMapping(path = GAME_CHECK_PLAYER)
    CheckPlayerResp checkIfPlayerExists(GameCheckPlayerReq gameCheckPlayer);
}
