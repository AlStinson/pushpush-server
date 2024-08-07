package com.pushpush.server.dto.message;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class StringMessage implements Message {
    MessageKind kind;
    String payload;

    public static StringMessage ofError(Throwable throwable) {
        return of(MessageKind.ERROR, throwable.getMessage());
    }
}
