package com.pushpush.server.dto.message;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SimpleMessage implements Message {
    MessageKind kind;
}
