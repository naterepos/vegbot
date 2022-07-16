package com.github.naterepos.vegbot.command.argument;

public class BooleanArgument extends Argument {

    public BooleanArgument(String key) {
        super(key);
    }

    @Override
    public Boolean formatInputIfValid(String rawInput) {
        return (rawInput.equalsIgnoreCase("true") || rawInput.equalsIgnoreCase("false")) ? Boolean.parseBoolean(rawInput) : null;
    }
}
