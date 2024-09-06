package com.pushpush.server.service;

import com.pushpush.server.model.GameSession;
import com.pushpush.server.model.SyncHashMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.websocket.Session;

import java.util.UUID;

public class SyncHashMapProvider {

    public static final String GAME_SESSION_BY_PLAYER = "gameSessionByPlayer";
    public static final String GAME_SESSION_BY_GAME_ID = "gameSessionByGameId";

    @ApplicationScoped
    @Named(GAME_SESSION_BY_PLAYER)
    public SyncHashMap<Session, GameSession> gameSessionByPlayer() {
        return new SyncHashMap<>();
    }

    @ApplicationScoped
    @Named(GAME_SESSION_BY_GAME_ID)
    public SyncHashMap<UUID, GameSession> gameSessionByGameId() {
        return new SyncHashMap<>();
    }
}
