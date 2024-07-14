package com.pushpush.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static com.pushpush.core.Team.BLACK;
import static com.pushpush.core.Team.NONE;
import static com.pushpush.core.Team.WHITE;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Piece {

    BALL(0, NONE),
    W1(1, WHITE),
    W2(2, WHITE),
    W3(3, WHITE),
    W4(4, WHITE),
    B1(1, BLACK),
    B2(2, BLACK),
    B3(3, BLACK),
    B4(4, BLACK);

    int force;
    Team team;

    public boolean belongsTo(Team t) {
        return team == t;
    }

    public boolean canMove(Piece other) {
        return force > other.force;
    }

    public boolean canKill(Piece other) {
        return force == 1 && other.force == 4 && team != other.team;
    }

    @Override
    public String toString() {
        return this == BALL ? "()" : super.toString();
    }
}