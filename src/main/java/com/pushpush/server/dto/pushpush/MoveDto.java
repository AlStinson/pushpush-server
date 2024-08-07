package com.pushpush.server.dto.pushpush;


import com.pushpush.core.Move;
import com.pushpush.core.MoveKind;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
public class MoveDto {
    Vector2IntDto init;
    Vector2IntDto dir;
    boolean normal;

    public Move toMove() {
        return new Move(init.toVector2Int(), dir.toVector2Int(), normal ? MoveKind.NORMAL : MoveKind.DEFLECTED);
    }

    public static MoveDto fromMove(Move move) {
        return MoveDto.builder()
                .init(Vector2IntDto.fromVector2Int(move.getInitialPosition()))
                .dir(Vector2IntDto.fromVector2Int(move.getDirection()))
                .normal(move.isNormal()).build();
    }

}
