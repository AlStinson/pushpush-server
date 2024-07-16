package com.pushpush.core;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Position {

    public final Vector2Int CENTER = new Vector2Int(4,4);

    public boolean areOpposite(Vector2Int first, Vector2Int second) {
        return first.isOpposite(second, CENTER);
    }
}
