package com.assignment.hr_service.common.exception;

/**
 * Thrown when a unique business constraint is violated (maps to HTTP 409).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
