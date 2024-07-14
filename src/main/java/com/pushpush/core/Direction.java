package com.pushpush.core;

import com.pushpush.core.utils.Vector2Int;
import com.pushpush.core.utils.zone.Rectangle;
import com.pushpush.core.utils.zone.Zone;
import com.pushpush.core.utils.zone.ZonesDifference;

import java.util.List;

public class Direction extends Vector2Int<Direction> {

    public static final Direction ZERO = new Direction(0,0);
    public static final Direction LEFT = new Direction(-1, 0);
    public static final Direction RIGHT = new Direction(1, 0);
    public static final Direction UP = new Direction(0, 1);
    public static final Direction DOWN = new Direction(0, -1);

    public static final Zone DIRECTION_ZONE = new ZonesDifference(new Rectangle(-1, 1), ZERO);
    public static final List<Direction> DIRECTIONS = ZERO.generateSquare(-1, 1).stream().filter(DIRECTION_ZONE::contains).toList();

    public Direction(int x, int y) {
        super(x, y);
    }

    @Override
    protected Direction create(int x, int y) {
        return new Direction(x, y);
    }

    @Override
    protected Direction center() {
        return ZERO;
    }
}
