package com.pushpush.server;

import com.pushpush.core.Game;
import com.pushpush.core.Move;
import jakarta.websocket.Session;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    Session white;
    Session black;
    List<Session> viewers = new ArrayList<>();
    Game game = new Game();

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

    public void remove(Session session) {
        if (white == session) white = null;
        else if (black == session) black = null;
        else viewers.remove(session);
    }

    public void play(Session session, Move move) {
        switch (game.getNextPlayer()) {
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

        game.makeMove(move);
    }


}
