package com.pushpush.server.exception;

public class UnexpectedMessageException extends RuntimeException {
    public UnexpectedMessageException(String message) {
        super(String.format("UnexpectedMessage: %s", message));
    }
}
