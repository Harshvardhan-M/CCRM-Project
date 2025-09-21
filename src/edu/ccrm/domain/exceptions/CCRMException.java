package edu.ccrm.domain.exceptions;

/**
 * Base exception class for CCRM application
 * Demonstrates custom exception hierarchy
 */
public class CCRMException extends Exception {
    
    private final String errorCode;
    private final java.time.LocalDateTime timestamp;
    
    public CCRMException(String message) {
        super(message);
        this.errorCode = "CCRM_ERROR";
        this.timestamp = java.time.LocalDateTime.now();
    }
    
    public CCRMException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = java.time.LocalDateTime.now();
    }
    
    public CCRMException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "CCRM_ERROR";
        this.timestamp = java.time.LocalDateTime.now();
    }
    
    public CCRMException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = java.time.LocalDateTime.now();
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public java.time.LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s (at %s)", 
            errorCode, getClass().getSimpleName(), getMessage(), timestamp);
    }
}