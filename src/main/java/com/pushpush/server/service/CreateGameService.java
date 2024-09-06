package com.pushpush.server.service;

import com.pushpush.server.exception.UnableToCreateGameException;
import com.pushpush.server.model.GameSession;
import com.pushpush.server.model.SyncHashMap;
import com.pushpush.server.model.TimeControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static com.pushpush.server.service.SyncHashMapProvider.GAME_SESSION_BY_GAME_ID;

@RequiredArgsConstructor
@ApplicationScoped
public class CreateGameService {

    static final int MAX_CREATE_GAME_TRIES = 100;


    @Named(GAME_SESSION_BY_GAME_ID)
    private final SyncHashMap<UUID, GameSession> gameSessionByGameId;

    public UUID createGame(TimeControl timeControl) {
        GameSession gameSession = new GameSession(timeControl);
        for (int i = 0; i < MAX_CREATE_GAME_TRIES; i++) {
            UUID uuid = UUID.randomUUID();
            if (gameSessionByGameId.putIfAbsent(uuid, gameSession)) return uuid;
        }
        throw new UnableToCreateGameException();
    }
}
