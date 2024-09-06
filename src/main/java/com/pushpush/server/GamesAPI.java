package com.pushpush.server;

import com.pushpush.server.model.TimeControl;
import com.pushpush.server.service.CreateGameService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Path("/v1/games")
public class GamesAPI {

    private final CreateGameService createGameService;

    @POST
    public UUID createGame(TimeControl timeControl) {
        return createGameService.createGame(timeControl);
    }
}
