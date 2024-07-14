package com.pushpush.core;

import org.junit.jupiter.api.Test;

import static com.pushpush.core.Direction.DOWN;
import static com.pushpush.core.Direction.UP;
import static com.pushpush.core.MoveKind.NORMAL;
import static com.pushpush.core.Team.NONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void initialBoardValidMove() {
        Game game = new Game();
        assertTrue(game.makeMove(new Move(new Position(1, 1), new Direction(0, 1), NORMAL)));
    }

    @Test
    void after3RepetitionsTheGameFinishAsDraw() {
        Move moveWhiteUp = new Move(new Position(1, 1), UP, NORMAL);
        Move moveWhiteDown = new Move(new Position(1, 2), DOWN, NORMAL);
        Move moveBlackUp = new Move(new Position(1, 6), UP, NORMAL);
        Move moveBlackDown = new Move(new Position(1, 7), DOWN, NORMAL);

        Game game = new Game();
        assertTrue(game.makeMove(moveWhiteUp));
        assertTrue(game.makeMove(moveBlackDown));
        assertTrue(game.makeMove(moveWhiteDown));
        assertTrue(game.makeMove(moveBlackUp));
        assertTrue(game.makeMove(moveWhiteUp));
        assertTrue(game.makeMove(moveBlackDown));
        assertTrue(game.makeMove(moveWhiteDown));
        assertTrue(game.makeMove(moveBlackUp));
        assertTrue(game.hasFinished());
        assertEquals(NONE, game.getWinner());

    }
}