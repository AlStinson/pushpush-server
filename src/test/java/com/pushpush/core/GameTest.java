package com.pushpush.core;

import org.junit.jupiter.api.Test;

import static com.pushpush.core.Direction.DOWN;
import static com.pushpush.core.Direction.UP;
import static com.pushpush.core.MoveKind.NORMAL;
import static com.pushpush.core.Team.NONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void initialBoardValidMove() {
        Game game = new Game();
        assertTrue(game.makeMove(new Move(new Vector2Int(1, 1), new Vector2Int(0, 1), NORMAL)));
    }

    @Test
    void after3RepetitionsTheGameFinishAsDraw() {
        Move moveWhiteUp = new Move(new Vector2Int(1, 1), UP, NORMAL);
        Move moveWhiteDown = new Move(new Vector2Int(1, 2), DOWN, NORMAL);
        Move moveBlackUp = new Move(new Vector2Int(1, 6), UP, NORMAL);
        Move moveBlackDown = new Move(new Vector2Int(1, 7), DOWN, NORMAL);

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

    @Test
    void symmetricMoveIsNotValid() {
        Game game = new Game();
        game.makeMove(move(4,1, UP, NORMAL));
        assertFalse(game.getValidMoves().contains(move(4, 7, DOWN, NORMAL)));
    }

    private Move move(int x, int y, Vector2Int dir, MoveKind kind) {
        return new Move(new Vector2Int(x, y), dir, kind);
    }
}