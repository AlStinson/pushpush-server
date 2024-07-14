package com.pushpush.server.dto;

import com.pushpush.core.Game;
import com.pushpush.core.Piece;
import com.pushpush.core.Team;
import lombok.Value;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class GameDto {
    Team winner;
    Team nextPlayer;
    Map<Vector2IntDto, Piece> board;
    List<MoveDto> validMoves;

    public static GameDto fromGame(Game game) {
        return new GameDto(
                game.getWinner(),
                game.getNextPlayer(),
                game.getBoard()
                        .entrySet()
                        .stream()
                        .map(entry -> new AbstractMap.SimpleEntry<>(
                                Vector2IntDto.fromVector2Int(entry.getKey()),
                                entry.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                game.getValidMoves().stream().map(MoveDto::fromMove).toList()
        );
    }

}
