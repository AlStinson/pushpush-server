package com.pushpush.core;

import org.junit.jupiter.api.Test;

import static com.pushpush.core.Piece.BALL;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void equivalentBoardsAreEquals() {
        Board board1 = new Board();
        Board board2 = new Board();
        board1.put(new Position(1, 2), BALL);
        board2.put(new Position(1, 2), BALL);
        assertEquals(board1, board2);
    }

    @Test
    void differentBoardsAreNotEquals() {
        assertNotEquals(new Board(), Board.getInitialBoard());
    }
}