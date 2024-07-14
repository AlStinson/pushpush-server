package com.pushpush.core;

public enum Team {

    WHITE,
    BLACK,
    NONE;

    public <T> T teamSwitch(T caseWhite, T caseBlack, T caseNone) {
        return switch (this) {
            case WHITE -> caseWhite;
            case BLACK -> caseBlack;
            default -> caseNone;
        };
    }

    public Team next() {
        return teamSwitch(BLACK, WHITE, NONE);
    }
}
