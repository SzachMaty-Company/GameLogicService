package com.szachmaty.gamelogicservice.controller.apiclient;

import com.szachmaty.gamelogicservice.data.dto.AIDataRequest;
import com.szachmaty.gamelogicservice.data.dto.AIDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "${external.service.ai-service.url}", name = "AI-CLIENT", configuration = FeignGameClientConfiguration.class)
public interface AIClient {
    @PostMapping(path = "/")
    AIDataResponse makeMove(AIDataRequest aiDataRequest);
}
