package com.szachmaty.gamelogicservice.controller.apiclient;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignGameClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new GameErrorDecoder();
    }
}
