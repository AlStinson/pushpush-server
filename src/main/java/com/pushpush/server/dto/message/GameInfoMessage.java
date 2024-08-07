package com.pushpush.server.dto.message;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

import static com.pushpush.server.dto.message.MessageKind.MATCHMAKING;

@Value
@Builder
public class GameInfoMessage implements Message {
    MessageKind kind;
    UUID gameId;
    String view;

    public static GameInfoMessage of(UUID gameId, String view) {
        return new GameInfoMessage(MATCHMAKING, gameId, view);
    }
}
