package com.pushpush.server.model;

import com.pushpush.core.Game;
import com.pushpush.core.Move;
import com.pushpush.core.Team;
import com.pushpush.server.dto.message.GameMessage;
import jakarta.websocket.Session;
import lombok.Locked;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    Session white;
    Session black;
    List<Session> viewers = new ArrayList<>();
    Game game = new Game();

    @Locked
    public boolean addAs(Session session, String kind) {
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
        if (white == session) white = null;
        else if (black == session) black = null;
        else viewers.remove(session);
    }

    @Locked
    public boolean play(Session session, Move move) {
        switch (game.getNextPlayer()) {
            case WHITE -> {
                if (session != white) return false;
            }
            case BLACK -> {
                if (session != black) return false;
            }
            default -> {
                return false;
            }
        }

        return game.makeMove(move);
    }

    @Locked
    public boolean surrender(Session session) {
        if (session == white) return game.setWinner(Team.BLACK);
        if (session == black) return game.setWinner(Team.WHITE);
        return false;
    }

    @Locked
    public boolean restart() {
        if (game.hasFinished()) {
            game = new Game();
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
        if (white != null) GameMessage.of(game, Team.WHITE, moved).sendTo(white);
        if (black != null) GameMessage.of(game, Team.BLACK, moved).sendTo(black);
        viewers.forEach(GameMessage.of(game, null, moved)::sendTo);
    }

}