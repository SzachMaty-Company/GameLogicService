package com.szachmaty.gamelogicservice.exception;

import org.springframework.http.HttpStatus;

public record ExceptionMessageDTO(String message, HttpStatus httpStatus) {}
