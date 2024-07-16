package com.pushpush.core;


import lombok.Value;

import static com.pushpush.core.Board.BLACK_BORDER_ZONE;
import static com.pushpush.core.Board.LEFT_BORDER_ZONE;
import static com.pushpush.core.Board.RIGHT_BORDER_ZONE;
import static com.pushpush.core.Board.WHITE_BORDER_ZONE;
import static com.pushpush.core.Direction.*;
import static com.pushpush.core.MoveKind.NORMAL;

@Value
public class Move {

    Vector2Int initialPosition;
    Vector2Int direction;
    MoveKind kind;

    public boolean isNormal() {
        return kind == NORMAL;
    }

    public boolean isOpposite(Move other) {
        return Position.areOpposite(initialPosition, other.initialPosition)
                && Direction.areOpposite(direction, other.direction)
                && isNormal() && other.isNormal();
    }

    public Vector2Int getFinalPosition() {
        return initialPosition.add(direction);
    }

    public Vector2Int getNextDirection() {
        Vector2Int nextDirection = direction;
        Vector2Int nextPosition = getFinalPosition();
        if (kind == MoveKind.DEFLECTED) {
            if (WHITE_BORDER_ZONE.contains(nextPosition)) nextDirection = nextDirection.add(UP);
            if (BLACK_BORDER_ZONE.contains(nextPosition)) nextDirection = nextDirection.add(DOWN);
            if (LEFT_BORDER_ZONE.contains(nextPosition)) nextDirection = nextDirection.add(RIGHT);
            if (RIGHT_BORDER_ZONE.contains(nextPosition)) nextDirection = nextDirection.add(LEFT);
        }
        return nextDirection;
    }

    public Move getNextMove() {
        return new Move(getFinalPosition(), getNextDirection(), NORMAL);
    }
}
