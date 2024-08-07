package com.pushpush.server.dto.message;

import com.pushpush.core.Game;
import com.pushpush.core.Team;
import com.pushpush.server.dto.pushpush.GameDto;
import lombok.Builder;
import lombok.Value;

import static com.pushpush.server.dto.message.MessageKind.GAME_UPDATE;
import static com.pushpush.server.dto.pushpush.GameDto.fromGame;

@Value
@Builder
public class GameMessage implements Message {
    MessageKind kind;
    GameDto game;

    public static GameMessage of(Game game, Team team, Boolean moved) {
        return new GameMessage(GAME_UPDATE, fromGame(game, team, moved));
    }
}
