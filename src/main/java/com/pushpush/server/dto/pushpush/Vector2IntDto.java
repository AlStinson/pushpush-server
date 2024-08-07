package com.pushpush.server.dto.pushpush;

import com.pushpush.core.Vector2Int;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
public class Vector2IntDto {
    int x;
    int y;

    public static Vector2IntDto fromVector2Int(Vector2Int vector) {
        return new Vector2IntDto(vector.getX(), vector.getY());
    }

    public Vector2Int toVector2Int() {
        return new Vector2Int(x, y);
    }
}
