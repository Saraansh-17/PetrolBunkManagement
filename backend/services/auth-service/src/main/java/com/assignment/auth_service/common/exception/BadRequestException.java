package com.assignment.auth_service.common.exception;

/**
 * Client error (400) with explicit message.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
