package com.pushpush.server.dto;


import com.pushpush.core.Move;
import com.pushpush.core.MoveKind;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MoveDto {

    Vector2IntDto init;
    Vector2IntDto dir;
    boolean normal;

    public static MoveDto fromMove(Move move) {
        return MoveDto.builder()
                .init(Vector2IntDto.fromVector2Int(move.getInitialPosition()))
                .dir(Vector2IntDto.fromVector2Int(move.getDirection()))
                .normal(move.isNormal())
                .build();
    }

    public Move toMove() {
        return new Move(init.toPosition(), dir.toDirection(), normal ? MoveKind.NORMAL : MoveKind.DEFLECTED);
    }

}
