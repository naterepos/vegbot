package com.github.naterepos.vegbot.command.argument;

public class OptionalArgument extends Argument {

    private final Argument possibleArgument;

    public OptionalArgument(String key, Argument possibleArgument) {
        super(key);
        this.possibleArgument = possibleArgument;
    }

    @Override
    public Object formatInputIfValid(String rawInput) {
        return possibleArgument.formatInputIfValid(rawInput);
    }
}
