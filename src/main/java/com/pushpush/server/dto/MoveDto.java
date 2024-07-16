package com.pushpush.server.dto;


import com.pushpush.core.Move;
import com.pushpush.core.MoveKind;
import com.pushpush.core.Vector2Int;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MoveDto {

    Vector2Int init;
    Vector2Int dir;
    boolean normal;

    public Move toMove() {
        return new Move(init, dir, normal ? MoveKind.NORMAL : MoveKind.DEFLECTED);
    }

    public static MoveDto fromMove(Move move) {
        return MoveDto.builder()
                .init(move.getInitialPosition())
                .dir(move.getDirection())
                .normal(move.isNormal())
                .build();
    }

}
