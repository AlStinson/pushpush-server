package com.pushpush.core;


import lombok.Value;

import static com.pushpush.core.Board.BLACK_BORDER_ZONE;
import static com.pushpush.core.Board.LEFT_BORDER_ZONE;
import static com.pushpush.core.Board.RIGHT_BORDER_ZONE;
import static com.pushpush.core.Board.WHITE_BORDER_ZONE;
import static com.pushpush.core.Direction.DOWN;
import static com.pushpush.core.Direction.LEFT;
import static com.pushpush.core.Direction.RIGHT;
import static com.pushpush.core.Direction.UP;
import static com.pushpush.core.MoveKind.NORMAL;

@Value
public class Move {

    Position initialPosition;
    Direction direction;
    MoveKind kind;

    public boolean isNormal() {
        return kind == NORMAL;
    }

    public boolean isOpposite(Move other) {
        return initialPosition.isOpposite(other.initialPosition)
                && direction.isOpposite(other.direction)
                && isNormal() && other.isNormal();
    }

    public Position getFinalPosition() {
        return initialPosition.add(direction);
    }

    public Direction getNextDirection() {
        Direction nextDirection = direction;
        if (kind == MoveKind.DEFLECTED) {
            if (WHITE_BORDER_ZONE.contains(initialPosition)) nextDirection = nextDirection.add(UP);
            if (BLACK_BORDER_ZONE.contains(initialPosition)) nextDirection = nextDirection.add(DOWN);
            if (LEFT_BORDER_ZONE.contains(initialPosition)) nextDirection = nextDirection.add(RIGHT);
            if (RIGHT_BORDER_ZONE.contains(initialPosition)) nextDirection = nextDirection.add(LEFT);
        }
        return nextDirection;
    }

    public Move getNextMove() {
        return new Move(getFinalPosition(), getNextDirection(), NORMAL);
    }
}
