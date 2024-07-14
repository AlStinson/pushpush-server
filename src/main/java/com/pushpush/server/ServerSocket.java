package com.pushpush.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pushpush.core.Game;
import com.pushpush.core.Move;
import com.pushpush.server.dto.GameDto;
import com.pushpush.server.dto.MoveDto;
import com.pushpush.server.dto.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ServerEndpoint("/ws")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ServerSocket {
    private final List<Session> sessions = new ArrayList<>();
    private final Game game = new Game();

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @OnOpen
    public void onOpen(Session session) {
        log.info("Connected " + session.getId());
        sessions.add(session);
        sendResponse(false).accept(session.getAsyncRemote());
    }

    @OnClose
    public void onClose(Session session) {
        log.info("Closed " + session.getId());
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn(session.getId(), throwable);
    }

    @SneakyThrows
    @OnMessage
    public void onMessage(Session session, String message) {
        log.trace("Message " + session.getId() + " " + message);
        Move move = objectMapper.readValue(message, MoveDto.class).toMove();
        boolean moveMade = game.makeMove(move);
        log.info("Move: {}, Made: ", move, moveMade);
        sessions.stream()
                .map(Session::getAsyncRemote)
                .forEach(sendResponse(moveMade));
    }

    private Consumer<RemoteEndpoint.Async> sendResponse(boolean moveMade) {
        return endpoint -> endpoint.sendText(getResponse(moveMade));
    }

    @SneakyThrows
    private String getResponse(boolean changed) {
        return objectMapper.writeValueAsString(new Response(changed, GameDto.fromGame(game)));
    }
}
