package com.pushpush.server.service;

import com.pushpush.server.model.GameSession;
import com.pushpush.server.model.SyncHashMap;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@ApplicationScoped
public class GameSessionsScheduler {

    @Named("gameSessionByGameId")
    private final SyncHashMap<UUID, GameSession> gameSessionByGameId;


    @Scheduled(every = "1m")
    public void cleanupGames() {
        gameSessionByGameId.removeIf(entry -> entry.getValue().isEmpty());
    }
}
