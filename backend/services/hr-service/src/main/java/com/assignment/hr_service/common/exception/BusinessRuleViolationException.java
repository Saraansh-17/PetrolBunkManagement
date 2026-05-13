package com.assignment.hr_service.common.exception;

/**
 * Thrown when domain rules are violated (maps to HTTP 400).
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
