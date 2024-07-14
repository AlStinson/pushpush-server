package com.pushpush.core.utils.zone;

import com.pushpush.core.utils.Vector2Int;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Rectangle implements Zone {

    int minX;
    int maxX;
    int minY;
    int maxY;

    public Rectangle(int min, int max) {
        minX = min;
        minY = min;
        maxX = max;
        maxY = max;
    }

    @Override
    public boolean contains(Vector2Int point) {
        boolean xBetween = isBetween(point.getX(), minX, maxX);
        boolean yBetween = isBetween(point.getY(), minY, maxY);
        return xBetween && yBetween;
    }

    private static boolean isBetween(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
