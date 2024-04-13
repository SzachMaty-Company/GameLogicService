package com.szachmaty.gamelogicservice.controller.apiclient;

import com.szachmaty.gamelogicservice.data.dto.GameFinishDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "${external.service.user-service.url}", name = "USER-DATA-SERVICE-CLIENT", configuration = FeignGameClientConfiguration.class)
public interface UserDataServiceClient {
    @PostMapping(path = "/game")
    void sendGame(GameFinishDTO game);
}


