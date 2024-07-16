package com.pushpush.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pushpush.core.Game;
import com.pushpush.core.Move;
import com.pushpush.server.dto.GameDto;
import com.pushpush.server.dto.Message;
import com.pushpush.server.dto.MoveDto;
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

@ServerEndpoint("/v1/ws")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ServerSocket {
    private final List<Session> sessions = new ArrayList<>();
    private Game game = new Game();

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @OnOpen
    public void onOpen(Session session) {
        log.info("Connected " + session.getId());
        sessions.add(session);
        sendResponse().accept(session.getAsyncRemote());
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
    public void onMessage(Session session, String string) {
        log.trace("Message " + session.getId() + " " + string);
        Message message = objectMapper.readValue(string, Message.class);
        switch (message.getKind()) {
            case "move" -> {
                Move move = getPayload(string, MoveDto.class).toMove();
                boolean moveMade = game.makeMove(move);
                log.info("Move: {}, Made: {}", move, moveMade);
            }
            case "reset" -> {
                game = new Game();
            }
            default -> {
                throw new IllegalArgumentException();
            }
        }

        sessions.stream()
                .map(Session::getAsyncRemote)
                .forEach(sendResponse());
    }

    private Consumer<RemoteEndpoint.Async> sendResponse() {
        return endpoint -> endpoint.sendText(getResponse());
    }

    @SneakyThrows
    private <T> T getPayload(String value, Class<T> payload) {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(Message.class, payload);
        Message<T> message = objectMapper.readValue(value, type);
        return message.getPayload();
    }

    @SneakyThrows
    private String getResponse() {
        return objectMapper.writeValueAsString(GameDto.fromGame(game));
    }
}
