package com.kydas.build.events;

public enum ActionType {
    SYSTEM("system"),
    WORK("work");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}