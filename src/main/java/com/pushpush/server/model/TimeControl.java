package com.pushpush.server.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class TimeControl {

    final int startingTimeMinutes;
    final int perMoveTimeSeconds;

}
