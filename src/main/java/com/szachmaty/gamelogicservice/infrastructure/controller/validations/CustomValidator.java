package com.szachmaty.gamelogicservice.infrastructure.controller.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequestValidator.class)
public @interface CustomValidator {
    String message() default "Invalid request input!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
