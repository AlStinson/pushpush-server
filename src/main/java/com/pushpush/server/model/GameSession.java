package com.pushpush.server.model;

import com.pushpush.core.Game;
import com.pushpush.core.Move;
import com.pushpush.core.Team;
import com.pushpush.server.dto.message.GameMessage;
import com.pushpush.server.model.clock.Clock;
import com.pushpush.server.util.ClockFactory;
import com.pushpush.server.util.FakeNameFactory;
import jakarta.websocket.Session;
import lombok.Locked;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.pushpush.server.dto.message.MessageKind.GAME_UPDATE;
import static com.pushpush.server.dto.pushpush.GameDto.fromGame;

@Slf4j
public class GameSession {

    final TimeControl timeControl;

    Session white;
    Session black;
    Clock clock;
    List<Session> viewers = new ArrayList<>();
    Game game = new Game();

    public GameSession(TimeControl timeControl) {
        this.timeControl = timeControl;
        this.clock = ClockFactory.fromTimeControl(timeControl);
    }

    @Locked
    public boolean addAs(Session session, String kind) {
        checkTime();
        boolean added = internalAddAs(session, kind);
        if (white != null && black != null) clock.startIfNotStarted();
        if (added) internalUpdateClients(false);
        return added;
    }

    private boolean internalAddAs(Session session, String kind) {
        switch (kind) {
            case "white" -> {
                if (white == null) {
                    white = session;
                    return true;
                }
                return false;
            }
            case "black" -> {
                if (black == null) {
                    black = session;
                    return true;
                }
                return false;
            }
            default -> {
                viewers.add(session);
                return true;
            }
        }
    }

    @Locked
    public void remove(Session session) {
        checkTime();
        if (white == session) white = null;
        else if (black == session) black = null;
        else viewers.remove(session);
    }

    @Locked
    public void play(Session session, Move move) {
        checkTime();

        Team nextPlayer = game.getNextPlayer();
        switch (nextPlayer) {
            case WHITE -> {
                if (session != white) return;
            }
            case BLACK -> {
                if (session != black) return;
            }
            default -> {
                return;
            }
        }

        boolean played = game.makeMove(move);
        if (played) clock.move(nextPlayer);
        internalUpdateClients(played);

    }

    @Locked
    public void surrender(Session session) {
        checkTime();
        internalUpdateClients(internalSurrender(session));
    }

    private boolean internalSurrender(Session session) {
        if (session == white) return game.setWinner(Team.BLACK);
        if (session == black) return game.setWinner(Team.WHITE);
        return false;
    }

    @Locked
    public void restart() {
        checkTime();
        internalUpdateClients(internalRestart());
    }

    private boolean internalRestart() {
        if (game.hasFinished()) {
            game = new Game();
            clock = ClockFactory.fromTimeControl(timeControl);
            clock.startIfNotStarted();
            return true;
        } else {
            return false;
        }
    }

    @Locked
    public boolean isEmpty() {
        return white == null && black == null && viewers.isEmpty();
    }


    @Locked
    public void updateClients(boolean moved) {
        checkTime();
        internalUpdateClients(moved);
    }

    private void internalUpdateClients(boolean moved) {
        if (white != null) toGameMessage(Team.WHITE, moved).sendTo(white);
        if (black != null) toGameMessage(Team.BLACK, moved).sendTo(black);
        viewers.forEach(toGameMessage(null, moved)::sendTo);
    }

    private void checkTime() {
        if (game.hasFinished()) return;
        boolean hasTime = clock.checkTime(game.getNextPlayer());
        if (!hasTime) game.setWinner(game.getNextPlayer().next());
    }

    private GameMessage toGameMessage(Team team, boolean moved) {
        return new GameMessage(
                GAME_UPDATE,
                fromGame(game, team, moved),
                FakeNameFactory.getName(white),
                FakeNameFactory.getName(black),
                clock.getTime(Team.WHITE),
                clock.getTime(Team.BLACK),
                clock.hasStarted());
    }
}
