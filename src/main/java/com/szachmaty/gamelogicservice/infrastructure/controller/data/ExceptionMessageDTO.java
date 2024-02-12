package com.szachmaty.gamelogicservice.infrastructure.controller.data;

import org.springframework.http.HttpStatus;

public record ExceptionMessageDTO(String message, HttpStatus httpStatus) {
}
