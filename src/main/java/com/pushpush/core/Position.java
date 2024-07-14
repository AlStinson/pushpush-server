package com.pushpush.core;

import com.pushpush.core.utils.Vector2Int;

public class Position extends Vector2Int<Position> {

    public static final Position CENTER = new Position(4,4);

    public Position(int x, int y) {
        super(x, y);
    }

    @Override
    protected Position create(int x, int y) {
        return new Position(x, y);
    }

    @Override
    protected Position center() {
        return CENTER;
    }
}
