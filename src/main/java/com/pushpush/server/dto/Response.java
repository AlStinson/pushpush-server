package com.pushpush.server.dto;

import lombok.Value;

@Value
public class Response {
    boolean lastCommand;
    GameDto gameDto;

}
