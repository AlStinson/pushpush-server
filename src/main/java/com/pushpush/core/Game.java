package com.pushpush.core;

import com.pushpush.core.utils.zone.Cross;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pushpush.core.Board.BALL_ZONE;
import static com.pushpush.core.Board.BOARD_ZONE;
import static com.pushpush.core.Board.BORDER_ZONE;
import static com.pushpush.core.Board.getGoalZone;
import static com.pushpush.core.Board.getInitialBoard;
import static com.pushpush.core.Direction.DIRECTIONS;
import static com.pushpush.core.Piece.BALL;
import static com.pushpush.core.Team.NONE;
import static com.pushpush.core.Team.WHITE;
import static com.pushpush.core.utils.CombinationsUtils.allCombinations3;
import static com.pushpush.core.utils.EnumUtils.listOf;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {

    public static final int MAX_TURNS = 200;

    Team winner;
    int turn;
    Team nextPlayer;
    Vector2Int ballPosition;
    final Board board;
    final List<Move> moves;
    final List<Move> validMoves;
    final List<Board> boardRecord;

    public Game() {
        this.winner = null;
        this.turn = 1;
        this.nextPlayer = WHITE;
        this.ballPosition = Position.CENTER;
        this.board = getInitialBoard();
        this.moves = new ArrayList<>();
        this.validMoves = new ArrayList<>();
        this.boardRecord = new ArrayList<>(Collections.singletonList(new Board(board)));
        generateValidMoves();
    }

    public boolean setWinner(Team t) {
        if (hasFinished()) return false;
        winner = t;
        nextPlayer = Team.NONE;
        validMoves.clear();
        return true;
    }

    public boolean hasFinished() {
        return winner != null;
    }

    public boolean makeMove(Move move) {
        if (!validMoves.contains(move)) return false;
        internalMakeMove(move);
        nextPlayer = nextPlayer.next();
        moves.add(move);
        turn++;
        boardRecord.add(new Board(board));
        generateValidMoves();
        checkWinner();
        return true;
    }

    private void checkWinner() {
        Team goalZone = getGoalZone(ballPosition);
        if (goalZone != Team.NONE) setWinner(goalZone.next());
        if (turn == MAX_TURNS) setWinner(Team.NONE);
        if (validMoves.isEmpty()) setWinner(nextPlayer.next());
        if (Collections.frequency(boardRecord, board) >= 3) setWinner(NONE);
    }

    private void internalMakeMove(Move move) {
        Piece piece1 = board.get(move.getInitialPosition());
        Piece piece2 = board.get(move.getFinalPosition());

        board.remove(move.getInitialPosition());
        if (piece1 == BALL) {
            board.put(move.getFinalPosition(), piece1);
            ballPosition = move.getFinalPosition();
            return;
        }
        if (piece2 != null && !piece1.canKill(piece2)) internalMakeMove(move.getNextMove());
        if (BOARD_ZONE.contains(move.getFinalPosition())) board.put(move.getFinalPosition(), piece1);
    }

    private void generateValidMoves() {
        if (winner != null) return;
        validMoves.clear();

        List<Vector2Int> validPositions = board.entrySet()
                .stream()
                .filter(k -> k.getValue().belongsTo(nextPlayer))
                .map(Map.Entry::getKey)
                .toList();

        allCombinations3(validPositions, DIRECTIONS, listOf(MoveKind.class), Move::new).forEach(this::addValidMove);
    }

    private void addValidMove(Move move) {
        if (isValidMove(move)) validMoves.add(move);
    }

    private boolean isValidMove(Move move) {
        if (turn == 2 && move.isOpposite(moves.get(0))) return false;
        Piece piece1 = board.get(move.getInitialPosition());
        if (piece1 == null) return false;
        if (!piece1.belongsTo(nextPlayer)) return false;
        if (!move.isNormal()) return isValidDeflectedMove(move);
        Piece piece2 = board.get(move.getFinalPosition());
        if (piece2 == null) return BOARD_ZONE.contains(move.getFinalPosition());
        if (piece1.canKill(piece2)) return true;
        if (!piece1.canMove(piece2)) return false;
        return isValidNextMove(move.getNextMove());
    }

    private boolean isValidDeflectedMove(Move move) {
        Piece piece2 = board.get(move.getFinalPosition());
        if (piece2 != BALL) return false;
        if (!BORDER_ZONE.contains(move.getInitialPosition())) return false;
        if (!move.getInitialPosition().isInZone(new Cross(move.getFinalPosition()))) return false;
        if (board.containsKey(move.getNextMove().getFinalPosition())) return false;
        return true;
    }

    private boolean isValidNextMove(Move move) {
        Piece piece1 = board.get(move.getInitialPosition());
        if (piece1 == BALL) return isValidBallMove(move);
        Piece piece2 = board.get(move.getFinalPosition());
        if (piece2 == null) return true;
        if (!piece1.canMove(piece2)) return false;

        return isValidNextMove(move.getNextMove());
    }

    private boolean isValidBallMove(Move move) {
        Piece piece2 = board.get(move.getFinalPosition());
        if (piece2 != null) return false;
        if (!BALL_ZONE.contains(move.getFinalPosition())) return false;
        if (getGoalZone(move.getFinalPosition()) == nextPlayer) return false;
        return true;
    }


    @Override
    public String toString() {
        return board.toString() + "\n" + (hasFinished() ?
                "Winner: " + winner :
                "Next turn: " + nextPlayer);
    }
}
