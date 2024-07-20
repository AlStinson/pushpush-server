package com.pushpush.server.dto;

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


    public static GameDto fromGame(Game game) {
        return new GameDto(
                game.getWinner(),
                game.getNextPlayer(),
                game.getBoard()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                game.getValidMoves().stream().map(MoveDto::fromMove).toList()
        );
    }

    public static GameDto fromGame(Game game, Team team) {
        return new GameDto(
                game.getWinner(),
                game.getNextPlayer(),
                game.getBoard()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                game.getNextPlayer() == team ?
                        game.getValidMoves().stream().map(MoveDto::fromMove).toList() :
                        Collections.emptyList()
        );
    }

}
