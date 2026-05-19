package com.assignment.auth_service.common.exception;

/**
 * Authentication failure (401).
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
