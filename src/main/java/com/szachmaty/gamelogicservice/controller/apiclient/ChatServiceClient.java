package com.szachmaty.gamelogicservice.controller.apiclient;

import com.szachmaty.gamelogicservice.data.dto.GameInitNotification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(url = "${external.service.chat-service.url}", name = "CHAT-SERVICE-CLIENT", configuration = FeignGameClientConfiguration.class)
public interface ChatServiceClient {
    @PostMapping(path = "/internal/game/invite")
    void iniviteToGame(GameInitNotification game);
}
