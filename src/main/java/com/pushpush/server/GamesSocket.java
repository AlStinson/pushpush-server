package com.pushpush.server;

import com.pushpush.core.Move;
import com.pushpush.server.dto.message.MoveMessage;
import com.pushpush.server.dto.message.SimpleMessage;
import com.pushpush.server.dto.message.StringMessage;
import com.pushpush.server.exception.SpotNotAvailableException;
import com.pushpush.server.exception.UnexpectedMessageException;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ServerEndpoint("/v3/ws/game/{gameId}/{kind}")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GamesSocket {
    private final Map<Session, GameSession> gamesSessionByPlayer = new HashMap<>();
    private final Map<UUID, GameSession> gameSessionByGameId = new HashMap<>();

    @OnOpen
    @SneakyThrows
    @Synchronized("gameSessionByGameId")
    public void onOpen(Session session, @PathParam("gameId") String gameIdString, @PathParam("kind") String kind) {
        log.info("{} connected to game {} as {} ", session.getId(), gameIdString, kind);
        UUID gameId = UUID.fromString(gameIdString);
        GameSession gameSession = gameSessionByGameId.containsKey(gameId) ? gameSessionByGameId.get(gameId) : createGameSession(gameId);
        boolean added = gameSession.addAs(session, kind);
        if (!added) throw new SpotNotAvailableException(gameId, kind);
        gamesSessionByPlayer.put(session, gameSession);
        gameSession.updateClients(false);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("{} disconnected", session.getId());
        GameSession gameSession = gamesSessionByPlayer.remove(session);
        if (gameSession != null) gameSession.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn(String.format("For client %s: ", session.getId()), throwable);
        if (session.isOpen()) {
            try {
                StringMessage.ofError(throwable).sendTo(session);
                session.close();
            } catch (Exception e) {
                log.error("Error closing session " + session.getId(), e);
            }
        }
    }

    @SneakyThrows
    @OnMessage
    public void onMessage(Session session, String content) {
        log.trace("Message " + session.getId() + " " + content);
        switch (SimpleMessage.of(content).getKind()) {
            case GAME_UPDATE -> {
                Move move = MoveMessage.of(content).getMove().toMove();
                GameSession gameSession = gamesSessionByPlayer.get(session);
                boolean moved = gameSession.play(session, move);
                gameSession.updateClients(moved);
            }
            case SURRENDER -> {
                GameSession gameSession = gamesSessionByPlayer.get(session);
                boolean gameEnded = gameSession.surrender(session);
                gameSession.updateClients(gameEnded);
            }
            default -> throw new UnexpectedMessageException(content);
        }

    }


    @Synchronized("gameSessionByGameId")
    private GameSession createGameSession(UUID gameId) {
        GameSession gameSession = new GameSession();
        gameSessionByGameId.put(gameId, gameSession);
        return gameSession;
    }

    @Synchronized("gameSessionByGameId")
    @Scheduled(every = "1m")
    public void cleanupGames() {
        gameSessionByGameId.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

}
