package com.pushpush.server.model.clock;

import com.pushpush.core.Team;
import com.pushpush.server.model.TimeControl;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class ClockImpl implements Clock {

    static final long MILLIS_IN_A_MINUTE = 1000L * 60L;
    static final long MILLIS_IN_A_SECOND = 1000L;

    final TimeControl timeControl;

    Instant lastCheck;

    long whiteTimeLeftMillis;
    long blackTimeLeftMillis;

    public ClockImpl(TimeControl timeControl) {
        this.timeControl = timeControl;
        whiteTimeLeftMillis = timeControl.getStartingTimeMinutes() * MILLIS_IN_A_MINUTE;
        blackTimeLeftMillis = timeControl.getStartingTimeMinutes() * MILLIS_IN_A_MINUTE;
    }

    @Override
    public void startIfNotStarted() {
        if (!hasStarted()) lastCheck = Instant.now();;
    }

    @Override
    public boolean checkTime(Team team) {
        if (!hasStarted()) return true;
        long time = getTime(team);
        if (time <= 0) return false;
        time -= elapseTime();
        time = Math.max(time, 0);
        lastCheck = Instant.now();
        setTime(team, time);
        return time > 0;
    }

    @Override
    public void move(Team team) {
        startIfNotStarted();
        setTime(team, getTime(team) + timeControl.getPerMoveTimeSeconds() * MILLIS_IN_A_SECOND);
    }

    @Override
    public Long getTime(Team team) {
        return team.teamSwitch(whiteTimeLeftMillis, blackTimeLeftMillis, null);
    }

    @Override
    public boolean hasStarted() {
        return lastCheck != null;
    }


    private void setTime(Team team, long value) {
        switch (team) {
            case WHITE -> whiteTimeLeftMillis = value;
            case BLACK -> blackTimeLeftMillis = value;
            default -> {
            }
        }
    }

    private long elapseTime() {
        return Instant.now().toEpochMilli() - lastCheck.toEpochMilli();
    }
}
