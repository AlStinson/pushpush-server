package com.pushpush.core.utils;

import com.pushpush.core.utils.zone.Zone;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.ArrayList;
import java.util.List;


@Value
@NonFinal
public abstract class Vector2Int<T extends Vector2Int<T>> implements Zone {

    int x;
    int y;

    protected abstract T create(int x, int y);

    protected abstract T center();

    public T add(Vector2Int<?> other) {
        return create(x + other.x, y + other.y);
    }

    public T minus(Vector2Int<?> other) {
        return create(x - other.x, y - other.y);
    }

    public T plus(int quantity) {
        return create(quantity * x, quantity * y);
    }

    public T getOpposite() {
        return center().plus(2).minus(this);
    }

    public boolean isOpposite(Vector2Int<?> other) {
        return other == getOpposite();
    }

    public boolean isInZone(Zone zone) {
        return zone.contains(this);
    }

    public List<T> generateSquare(int min, int max) {
        return generateRectangle(min, max, min, max);
    }

    public List<T> generateRectangle(int minX, int maxX, int minY, int maxY) {
        int xs = maxX - minX + 1;
        int ys = maxY - minY + 1;
        List<T> vectors = new ArrayList<>(xs * ys);
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                vectors.add(create(i, j));
            }
        }
        return vectors;
    }

    @Override
    public boolean contains(Vector2Int point) {
        return equals(point);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
