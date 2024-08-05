package com.pushpush.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pushpush.core.Move;
import com.pushpush.server.dto.MoveDto;
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
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@ServerEndpoint("/v2/ws/{gameId}/{kind}")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ServerSocket {
    private final GamesManager gamesManager;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") String gameIdString, @PathParam("kind") String kind) {
        log.info("Connected " + session.getId());
        UUID gameId = UUID.fromString(gameIdString);
        if (gamesManager.addAs(session, gameId, kind)) return;

        log.warn(session.getId() + " cannot play as " + kind);
        session.getAsyncRemote().sendText("Error: cannot play as " + kind);
        session.close();

    }

    @OnClose
    public void onClose(Session session) {
        log.info("Closed " + session.getId());
        gamesManager.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn(session.getId(), throwable);
    }

    @SneakyThrows
    @OnMessage
    public void onMessage(Session session, String string) {
        log.trace("Message " + session.getId() + " " + string);
        MoveDto message = objectMapper.readValue(string, MoveDto.class);
        Move move = message.toMove();
        gamesManager.play(session, move);
    }


}
