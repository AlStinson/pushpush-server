package com.pushpush.server;

import com.pushpush.core.Move;
import com.pushpush.server.dto.message.MoveMessage;
import com.pushpush.server.dto.message.SimpleMessage;
import com.pushpush.server.dto.message.StringMessage;
import com.pushpush.server.exception.GameDoNotExists;
import com.pushpush.server.exception.SpotNotAvailableException;
import com.pushpush.server.exception.UnexpectedMessageException;
import com.pushpush.server.model.GameSession;
import com.pushpush.server.model.SyncHashMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@ServerEndpoint("/v3/ws/game/{gameId}/{kind}")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GamesSocket {

    @Named("gameSessionByPlayer")
    private final SyncHashMap<Session, GameSession> gameSessionByPlayer;

    @Named("gameSessionByGameId")
    private final SyncHashMap<UUID, GameSession> gameSessionByGameId;

    @OnOpen
    @SneakyThrows
    public void onOpen(Session session, @PathParam("gameId") String gameIdString, @PathParam("kind") String kind) {
        log.info("{} connected to game {} as {} ", session.getId(), gameIdString, kind);
        UUID gameId = UUID.fromString(gameIdString);
        GameSession gameSession =  gameSessionByGameId.get(gameId);
        if (gameSession == null) throw new GameDoNotExists(gameId);
        boolean added = gameSession.addAs(session, kind);
        if (!added) throw new SpotNotAvailableException(gameId, kind);
        gameSessionByPlayer.put(session, gameSession);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("{} disconnected", session.getId());
        GameSession gameSession = gameSessionByPlayer.remove(session);
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
            case MOVE -> {
                Move move = MoveMessage.of(content).getMove().toMove();
                GameSession gameSession = gameSessionByPlayer.get(session);
                gameSession.play(session, move);
            }
            case GAME_UPDATE -> {
                GameSession gameSession = gameSessionByPlayer.get(session);
                gameSession.updateClients(false);
            }
            case SURRENDER -> {
                GameSession gameSession = gameSessionByPlayer.get(session);
                gameSession.surrender(session);
            }
            case RESTART -> {
                GameSession gameSession = gameSessionByPlayer.get(session);
                gameSession.restart();
            }
            default -> throw new UnexpectedMessageException(content);
        }

    }

}
