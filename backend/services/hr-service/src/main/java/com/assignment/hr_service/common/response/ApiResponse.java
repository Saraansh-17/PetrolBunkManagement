package com.assignment.hr_service.common.response;

import java.time.Instant;

/**
 * Standard envelope for all REST API responses.
 * Keeps the presentation layer contract stable while domain/DTO shapes evolve.
 */
public class ApiResponse<T> {

    private Instant timestamp;
    private int status;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(Instant timestamp, int status, String message, T data) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse<>(Instant.now(), status, message, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return of(200, message, data);
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{"
                + "timestamp=" + timestamp
                + ", status=" + status
                + ", message='" + message + '\''
                + ", data=" + data
                + '}';
    }
}
