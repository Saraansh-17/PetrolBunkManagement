package com.assignment.hr_service.common.util;

import java.time.format.DateTimeFormatter;

/**
 * Shared date/time patterns for parsing and formatting across controllers and services.
 */
public final class DatePatterns {

    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    private DatePatterns() {
    }
}
