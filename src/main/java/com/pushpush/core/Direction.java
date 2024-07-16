package com.pushpush.core;

import com.pushpush.core.utils.zone.Rectangle;
import com.pushpush.core.utils.zone.Zone;
import com.pushpush.core.utils.zone.ZonesDifference;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class Direction {

    public static final Vector2Int ZERO = new Vector2Int(0, 0);
    public static final Vector2Int LEFT = new Vector2Int(-1, 0);
    public static final Vector2Int RIGHT = new Vector2Int(1, 0);
    public static final Vector2Int UP = new Vector2Int(0, 1);
    public static final Vector2Int DOWN = new Vector2Int(0, -1);

    public static final Zone DIRECTION_ZONE = new ZonesDifference(new Rectangle(-1, 1), ZERO);
    public static final List<Vector2Int> DIRECTIONS = ZERO.generateSquare(-1, 1).stream().filter(DIRECTION_ZONE::contains).toList();


    public boolean areOpposite(Vector2Int first, Vector2Int second) {
        return first.isOpposite(second, ZERO);
    }
}
