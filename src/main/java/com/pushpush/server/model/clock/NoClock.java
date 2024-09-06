package com.pushpush.server.model.clock;

import com.pushpush.core.Team;

public class NoClock implements Clock {


    @Override
    public void startIfNotStarted() {

    }

    @Override
    public boolean checkTime(Team team) {
        return true;
    }

    @Override
    public void move(Team kind) {

    }

    @Override
    public Long getTime(Team team) {
        return null;
    }

    @Override
    public boolean hasStarted() {
        return false;
    }

}
