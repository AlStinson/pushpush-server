package com.pushpush.server.dto;

import com.pushpush.core.Direction;
import com.pushpush.core.Position;
import com.pushpush.core.utils.Vector2Int;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Vector2IntDto {
    int x;
    int y;

    public static Vector2IntDto fromVector2Int(Vector2Int<?> v) {
        return new Vector2IntDto(v.getX(), v.getY());
    }

    public Direction toDirection() {
        return new Direction(x, y);
    }

    public Position toPosition() {
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return x + "" + y;
    }
}
