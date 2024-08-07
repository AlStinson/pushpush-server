package com.pushpush.server.exception;

import java.util.UUID;

public class SpotNotAvailableException extends RuntimeException {
    public SpotNotAvailableException(UUID gameId, String kind) {
        super(String.format("SpotNotAvailable: %s %s", gameId, kind));
    }
}
