package com.pushpush.server.dto.message;

import lombok.Value;

@Value(staticConstructor = "of")
public class StringMessage implements Message {
    MessageKind kind;
    String payload;

    public static StringMessage ofError(Throwable throwable) {
        return of(MessageKind.ERROR, throwable.getMessage());
    }
}
