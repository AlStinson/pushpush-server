package com.pushpush.core;

import com.pushpush.core.utils.zone.Zone;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;


@Value
public class Vector2Int implements Zone {

    int x;
    int y;

    public Vector2Int add(Vector2Int other) {
        return new Vector2Int(x + other.x, y + other.y);
    }

    public Vector2Int minus(Vector2Int other) {
        return new Vector2Int(x - other.x, y - other.y);
    }

    public Vector2Int plus(int quantity) {
        return new Vector2Int(quantity * x, quantity * y);
    }

    public Vector2Int getOpposite(Vector2Int center) {
        return center.plus(2).minus(this);
    }

    public boolean isOpposite(Vector2Int other, Vector2Int center) {
        return other.equals(getOpposite(center));
    }

    public boolean isInZone(Zone zone) {
        return zone.contains(this);
    }

    public List<Vector2Int> generateSquare(int min, int max) {
        return generateRectangle(min, max, min, max);
    }

    public List<Vector2Int> generateRectangle(int minX, int maxX, int minY, int maxY) {
        int xs = maxX - minX + 1;
        int ys = maxY - minY + 1;
        List<Vector2Int> vectors = new ArrayList<>(xs * ys);
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                vectors.add(new Vector2Int(i, j));
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
