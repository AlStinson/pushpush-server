package com.pushpush.server;

import com.pushpush.server.dto.message.GameInfoMessage;
import com.pushpush.server.dto.message.StringMessage;
import com.pushpush.server.exception.UnexpectedMessageException;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

@ServerEndpoint("/v3/ws/matchmaking")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class MatchmakingSocket {
    private final LinkedBlockingQueue<Session> queue = new LinkedBlockingQueue<>();

    @OnOpen
    @Synchronized("queue")
    public void onOpen(Session session) {
        log.info("{} joined the matchmaking queue", session.getId());
        queue.add(session);
    }

    @OnClose
    @Synchronized("queue")
    public void onClose(Session session) {
        log.info("{} leaved the matchmaking queue", session.getId());
        queue.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn(String.format("%s for client %s", session.getId(), throwable.getMessage()), throwable);
        if (session.isOpen()) {
            try {
                StringMessage.ofError(throwable).sendTo(session);
                session.close();
            } catch (Exception e) {
                log.error("Error closing session " + session.getId(), e);
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, String content) {
        throw new UnexpectedMessageException(content);
    }

    @Scheduled(every = "5s")
    @Synchronized("queue")
    public void findMatch() {
        while (queue.size() > 1) {
            List<Session> players = new LinkedList<>();
            players.add(queue.remove());
            players.add(queue.remove());
            Collections.shuffle(players);
            UUID gameId = UUID.randomUUID();
            Session white = players.remove(0);
            Session black = players.remove(0);
            GameInfoMessage.of(gameId, "white").sendTo(white);
            GameInfoMessage.of(gameId, "black").sendTo(black);
            log.info("Game found between {} and {}", white.getId(), black.getId());
        }
    }
}
