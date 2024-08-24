package com.pushpush.server.service;

import com.pushpush.server.model.GameSession;
import com.pushpush.server.model.SyncHashMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.websocket.Session;

import java.util.UUID;

public class SyncHashMapProvider {

    @ApplicationScoped
    @Named("gameSessionByPlayer")
    public SyncHashMap<Session, GameSession> gameSessionByPlayer() {
        return new SyncHashMap<>();
    }

    @ApplicationScoped
    @Named("gameSessionByGameId")
    public SyncHashMap<UUID, GameSession> gameSessionByGameId() {
        return new SyncHashMap<>();
    }
}
