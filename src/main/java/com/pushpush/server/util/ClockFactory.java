package com.pushpush.server.util;

import com.pushpush.server.model.clock.Clock;
import com.pushpush.server.model.clock.ClockImpl;
import com.pushpush.server.model.clock.NoClock;
import com.pushpush.server.model.TimeControl;

public class ClockFactory {
    public static Clock fromTimeControl(TimeControl timeControl) {
        return timeControl == null || timeControl.getStartingTimeMinutes() == 0 ? new NoClock() : new ClockImpl(timeControl);
    }
}
