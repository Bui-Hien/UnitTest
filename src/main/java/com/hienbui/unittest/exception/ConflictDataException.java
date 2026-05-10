package com.hienbui.unittest.exception;

public class ConflictDataException extends RuntimeException {

    public ConflictDataException(String message) {
        super(message);
    }

    public ConflictDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
