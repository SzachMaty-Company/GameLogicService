package com.szachmaty.gamelogicservice.controller.apiclient;

import com.szachmaty.gamelogicservice.exception.GameClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
@Slf4j
public class GameErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String resErrorCause = response.reason();
        log.error(methodKey);
        log.error(resErrorCause);
        log.error(response.body().toString());
        System.out.println(resErrorCause); //TO BE ADDED
        HttpStatus httpResStatus = HttpStatus.valueOf(response.status());
        log.error(String.valueOf(httpResStatus));
        if(httpResStatus.is5xxServerError()) {
            throw new GameClientException("Internal server error!", httpResStatus);
        } else if(httpResStatus.is4xxClientError()) {
            throw new GameClientException("Client error", httpResStatus);
        }
        return null;
    }
}
