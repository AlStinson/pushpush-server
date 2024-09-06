package com.pushpush.server.util;

import com.github.javafaker.Faker;
import jakarta.websocket.Session;
import lombok.Locked;

import java.util.Locale;
import java.util.Random;

public class FakeNameFactory {

    private static final Random random = new Random();
    private static final Faker faker = new Faker(random);

    @Locked
    public static String getName(Session session) {
        if (session == null) return null;
        random.setSeed(session.getId().hashCode());
        return faker.name().name();

    }
}
