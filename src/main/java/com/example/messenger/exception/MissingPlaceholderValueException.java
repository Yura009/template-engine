package com.example.messenger.exception;

public class MissingPlaceholderValueException extends RuntimeException {
    public MissingPlaceholderValueException(String placeholder) {
        super("Missing value for placeholder: " + placeholder);
    }
}
