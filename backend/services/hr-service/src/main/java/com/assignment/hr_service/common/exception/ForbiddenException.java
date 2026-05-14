package com.assignment.hr_service.common.exception;

/**
 * Authorization failure (HTTP 403).
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
