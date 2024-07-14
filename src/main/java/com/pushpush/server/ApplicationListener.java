package com.pushpush.server;

import com.pushpush.core.Direction;
import com.pushpush.core.Game;
import com.pushpush.core.Move;
import com.pushpush.core.MoveKind;
import com.pushpush.core.Position;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ApplicationListener {

    void onStart(@Observes StartupEvent ev) {
        Game game = new Game();
        print(game);
        print(game.makeMove(new Move(new Position(4,4), new Direction(1,0), MoveKind.NORMAL)));
        print(game.makeMove(new Move(new Position(1,1), new Direction(0,1), MoveKind.NORMAL)));
        print(game);

    }

    void print(Object object) {
        log.info(object.toString());
    }
}
