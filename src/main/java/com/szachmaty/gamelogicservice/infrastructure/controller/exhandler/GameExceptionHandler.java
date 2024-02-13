package com.szachmaty.gamelogicservice.infrastructure.controller.exhandler;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.GameClientException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

@RestControllerAdvice
public class GameExceptionHandler {

    //TO-BE CHANGED - ONLY FOR TESTS
    @ExceptionHandler({ GameClientException.class })
    @SuppressWarnings("rawtypes")
    public ResponseEntity gameClientExceptionHandler(GameClientException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
    @ExceptionHandler({ FeignException.class })
    @SuppressWarnings("rawtypes")
    public ResponseEntity connectionErrorHandler(ConnectException e) {
        return new ResponseEntity("Network Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
