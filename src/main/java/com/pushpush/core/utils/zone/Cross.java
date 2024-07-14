package com.pushpush.core.utils.zone;

import com.pushpush.core.utils.Vector2Int;
import lombok.Value;

@Value
public class Cross implements Zone {

    Vector2Int<?> center;

    @Override
    public boolean contains(Vector2Int point) {
        return center.getX() == point.getX() || center.getY() == point.getY();
    }
}
