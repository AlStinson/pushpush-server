package com.pushpush.core.utils.zone;

import com.pushpush.core.utils.Vector2Int;
import lombok.Value;

import java.util.Arrays;

@Value
public class ZonesUnion implements Zone {

    Zone[] zonesArray;

    public ZonesUnion(Zone... zones) {
        zonesArray = zones;
    }

    @Override
    public boolean contains(Vector2Int point) {
        return Arrays.stream(zonesArray).anyMatch(point::isInZone);
    }
}
