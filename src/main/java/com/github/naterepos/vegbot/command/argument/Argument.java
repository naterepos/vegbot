package com.github.naterepos.vegbot.command.argument;

public abstract class Argument {

    private final String key;

    protected Argument(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public abstract Object formatInputIfValid(String rawInput);
}
