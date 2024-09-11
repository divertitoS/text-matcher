package com.bigid.textmatcher.exception;

public class InvalidOffsetException extends RuntimeException {
    public InvalidOffsetException(String message) {
        super(message);
    }

    public InvalidOffsetException(String message, Throwable cause) {
        super(message, cause);
    }
}
