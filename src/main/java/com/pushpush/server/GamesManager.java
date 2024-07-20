package com.pushpush.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pushpush.core.Game;
import com.pushpush.core.Move;
import com.pushpush.core.Team;
import com.pushpush.server.dto.GameDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@ApplicationScoped
@RequiredArgsConstructor
public class GamesManager {

    private final Map<Session, GameSession> gamesSessionByPlayer = new HashMap<>();
    private final Map<UUID, GameSession> gameSessionByGameId = new HashMap<>();

    private final ObjectMapper objectMapper;

    public boolean addAs(Session session, UUID gameId, String kind) {
        GameSession gameSession = gameSessionByGameId.containsKey(gameId) ?
                gameSessionByGameId.get(gameId) :
                createGameSession(gameId);
        boolean added = gameSession.addAs(session, kind);
        if (added) {
            gamesSessionByPlayer.put(session, gameSession);
            updateClients(gameSession);

        }
        return added;
    }

    public void remove(Session session) {
        GameSession gameSession = gamesSessionByPlayer.remove(session);
        if (gameSession != null) gameSession.remove(session);
    }

    public void play(Session session, Move move) {
        GameSession gameSession = gamesSessionByPlayer.get(session);
        gameSession.play(session, move);
        updateClients(gameSession);
    }

    private void updateClients(GameSession gameSession) {
        if (gameSession.white != null) sendResponse(GameDto.fromGame(gameSession.game, Team.WHITE)).accept(gameSession.white);
        if (gameSession.black != null) sendResponse(GameDto.fromGame(gameSession.game, Team.BLACK)).accept(gameSession.black);
        gameSession.viewers.stream().forEach(sendResponse(GameDto.fromGame(gameSession.game, null)));
    }

    private GameSession createGameSession(UUID gameId) {
        GameSession gameSession = new GameSession();
        gameSessionByGameId.put(gameId, gameSession);
        return gameSession;
    }

    @SneakyThrows
    private Consumer<Session> sendResponse(GameDto game) {
        String response = objectMapper.writeValueAsString(game);
        return session -> session.getAsyncRemote().sendText(response);
    }

}
