package com.pushpush.server.model.clock;

import com.pushpush.core.Team;

public interface Clock {

    void startIfNotStarted();

    boolean checkTime(Team kind);

    void move(Team kind);

    Long getTime(Team team);

    boolean hasStarted();
}
