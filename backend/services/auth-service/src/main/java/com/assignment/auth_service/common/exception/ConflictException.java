package com.assignment.auth_service.common.exception;

/**
 * Resource conflict such as duplicate email (409).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
