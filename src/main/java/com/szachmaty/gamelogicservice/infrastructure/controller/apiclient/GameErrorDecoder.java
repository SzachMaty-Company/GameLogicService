package com.szachmaty.gamelogicservice.infrastructure.controller.apiclient;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class GameErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String requestURL = response.request().url();
        HttpStatus httpResStatus = HttpStatus.valueOf(response.status());
        if(httpResStatus.is5xxServerError()) {
            throw new RuntimeException("ERROR 500!");
        } else if(httpResStatus.is4xxClientError()) {
            throw new RuntimeException("ERROR 400!");
        } else {
            return new Exception("BLAD");
        }
    }
}
