package com.szachmaty.gamelogicservice.infrastructure.controller.exhandler;

import com.szachmaty.gamelogicservice.infrastructure.controller.data.ExceptionMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameExceptionHandler {

    //TO-BE CHANGED - ONLY FOR TESTS
    @ExceptionHandler({ RuntimeException.class })
    @SuppressWarnings("rawtypes")
    public ResponseEntity globalExHandler(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
