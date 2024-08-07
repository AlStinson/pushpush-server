package com.pushpush.server.dto.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import lombok.SneakyThrows;

public interface Message {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @SneakyThrows
    static <T extends Message> T readValue(String content, Class<T> tClass) {
        return OBJECT_MAPPER.readValue(content, tClass);
    }

    @SneakyThrows
    default void sendTo(Session session) {
        session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(this));
    }
}
