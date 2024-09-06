package com.pushpush.server.dto.message;

import com.pushpush.server.dto.pushpush.GameDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class GameMessage implements Message {
    MessageKind kind;
    GameDto game;
    String whiteName;
    String blackName;
    Long whiteTimeLeftMillis;
    Long blackTimeLeftMillis;
    boolean hasStarted;
}
