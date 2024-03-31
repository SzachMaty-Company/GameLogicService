package com.szachmaty.gamelogicservice.controller.apiclient;

import com.szachmaty.gamelogicservice.data.dto.GameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.szachmaty.gamelogicservice.controller.APIRoutes.GAME_FINISH;


@FeignClient(url = "${external.service.user-service.url}", name = "GAME-CLIENT", configuration = FeignGameClientConfiguration.class)
public interface GameClient {
    @GetMapping("/entries")
    List<Object> getTestEntries();
    @PostMapping(path = GAME_FINISH)
    void sendGameAfterFinish(GameDTO game);
}
