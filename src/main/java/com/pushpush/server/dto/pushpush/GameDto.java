package com.pushpush.server.dto.pushpush;

import com.pushpush.core.Game;
import com.pushpush.core.Piece;
import com.pushpush.core.Team;
import com.pushpush.core.Vector2Int;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class GameDto {
    Team winner;
    Team nextPlayer;
    Map<Vector2Int, Piece> board;
    List<MoveDto> validMoves;
    boolean moved;

    public static GameDto fromGame(Game game, Team team, boolean moved) {
        return new GameDto(
                game.getWinner(),
                game.getNextPlayer(),
                game.getBoard()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                game.getNextPlayer() == team ?
                        game.getValidMoves().stream().map(MoveDto::fromMove).toList() :
                        Collections.emptyList(),
                moved
        );
    }

}
