package com.assignment.hr_service.common.exception;

/**
 * Thrown when a requested aggregate cannot be found (maps to HTTP 404).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
