package com.pushpush.core.utils.zone;

import com.pushpush.core.utils.Vector2Int;
import lombok.Value;

@Value
public class ZonesDifference implements Zone {

    Zone validZone;
    Zone invalidZone;

    @Override
    public boolean contains(Vector2Int point) {
        return validZone.contains(point) && !invalidZone.contains(point);
    }
}
