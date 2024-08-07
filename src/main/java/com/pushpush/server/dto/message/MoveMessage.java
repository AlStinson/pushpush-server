package com.pushpush.server.dto.message;

import com.pushpush.server.dto.pushpush.MoveDto;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MoveMessage implements Message {
    MessageKind kind;
    MoveDto move;

    public static MoveMessage of(String content) {
        return Message.readValue(content, MoveMessage.class);
    }
}
