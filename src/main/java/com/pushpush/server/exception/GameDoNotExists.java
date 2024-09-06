package com.pushpush.server.exception;

import java.util.UUID;

public class GameDoNotExists extends RuntimeException {
    public GameDoNotExists(UUID gameId) {
        super(String.format("Game do not exists: %s", gameId));
    }
}