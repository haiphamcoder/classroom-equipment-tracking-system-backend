package com.classroom.equipment.config.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}