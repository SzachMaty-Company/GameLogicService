package com.szachmaty.gamelogicservice.infrastructure.controller.apiclient;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class GameErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String resErrorCause = response.reason();
        System.out.println(resErrorCause); //TO BE ADDED
        HttpStatus httpResStatus = HttpStatus.valueOf(response.status());
        if(httpResStatus.is5xxServerError()) {
            throw new GameClientException("Internal server error!", httpResStatus);
        } else if(httpResStatus.is4xxClientError()) {
            throw new GameClientException("Client error", httpResStatus);
        }
        return null;
    }
}
