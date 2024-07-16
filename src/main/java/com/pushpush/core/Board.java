package com.pushpush.core;

import com.pushpush.core.utils.zone.Rectangle;
import com.pushpush.core.utils.zone.Zone;
import com.pushpush.core.utils.zone.ZonesUnion;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.pushpush.core.Piece.B1;
import static com.pushpush.core.Piece.B2;
import static com.pushpush.core.Piece.B3;
import static com.pushpush.core.Piece.B4;
import static com.pushpush.core.Piece.BALL;
import static com.pushpush.core.Piece.W1;
import static com.pushpush.core.Piece.W2;
import static com.pushpush.core.Piece.W3;
import static com.pushpush.core.Piece.W4;

@NoArgsConstructor
public class Board extends HashMap<Vector2Int, Piece> {

    public Board(Map<Vector2Int, Piece> board) {
        super(board);
    }

    public static final Zone WHITE_GOAL_ZONE = new Rectangle(1, 7, 0, 0);
    public static final Zone BLACK_GOAL_ZONE = new Rectangle(1, 7, 8, 8);
    public static final Zone WHITE_BORDER_ZONE = new Rectangle(1, 7, 1, 1);
    public static final Zone BLACK_BORDER_ZONE = new Rectangle(1, 7, 7, 7);
    public static final Zone LEFT_BORDER_ZONE = new Rectangle(1, 1, 1, 7);
    public static final Zone RIGHT_BORDER_ZONE = new Rectangle(7, 7, 1, 7);
    public static final Zone BORDER_ZONE = new ZonesUnion(WHITE_BORDER_ZONE, BLACK_BORDER_ZONE, LEFT_BORDER_ZONE, RIGHT_BORDER_ZONE);
    public static final Zone BOARD_ZONE = new Rectangle(1, 7);
    public static final Zone BALL_ZONE = new ZonesUnion(BOARD_ZONE, WHITE_GOAL_ZONE, BLACK_GOAL_ZONE);

    public static Board getInitialBoard() {
        Board board = new Board();
        board.put(1, 1, W1);
        board.put(2, 1, W2);
        board.put(3, 1, W3);
        board.put(4, 1, W4);
        board.put(5, 1, W3);
        board.put(6, 1, W2);
        board.put(7, 1, W1);
        board.put(1, 7, B1);
        board.put(2, 7, B2);
        board.put(3, 7, B3);
        board.put(4, 7, B4);
        board.put(5, 7, B3);
        board.put(6, 7, B2);
        board.put(7, 7, B1);
        board.put(4, 4, BALL);
        return board;
    }

    public static Team getGoalZone(Vector2Int position) {
        if (WHITE_GOAL_ZONE.contains(position)) return Team.WHITE;
        if (BLACK_GOAL_ZONE.contains(position)) return Team.BLACK;
        return Team.NONE;
    }

    private void put(int x, int y, Piece p) {
        put(new Vector2Int(x, y), p);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n------------------------------------\n");
        for (int y = 7; y > 0; y--) {
            builder.append("|");
            for (int x = 1; x <= 7; x++) {
                Piece piece = get(new Vector2Int(x, y));
                builder.append(" ");
                builder.append(piece == null ? "  " : piece.toString());
                builder.append(" |");
            }
            builder.append("\n------------------------------------\n");
        }
        return builder.toString();
    }
}
