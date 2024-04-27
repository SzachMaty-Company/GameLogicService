package com.szachmaty.gamelogicservice.controller.apiclient;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignGameClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new GameErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
