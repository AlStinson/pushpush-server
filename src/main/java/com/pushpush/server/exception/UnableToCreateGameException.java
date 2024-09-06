package com.pushpush.server.exception;

public class UnableToCreateGameException extends RuntimeException {
    public UnableToCreateGameException() {
        super("Unable to create game");
    }

}
