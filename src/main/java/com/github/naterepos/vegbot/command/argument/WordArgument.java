package com.github.naterepos.vegbot.command.argument;

public class WordArgument extends Argument {

    public WordArgument(String key) {
        super(key);
    }

    @Override
    public String formatInputIfValid(String rawInput) {
        return !rawInput.contains(" ") ? rawInput : null;
    }
}
