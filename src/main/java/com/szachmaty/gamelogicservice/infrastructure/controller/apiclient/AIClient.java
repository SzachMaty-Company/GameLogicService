package com.szachmaty.gamelogicservice.infrastructure.controller.apiclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "${external.service.ai-service.url}", name = "AI-CLIENT", configuration = FeignGameClientConfiguration.class)
public interface AIClient {
    @PostMapping(path = "/")
    String makeMove(String board);
}
