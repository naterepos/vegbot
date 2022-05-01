package com.github.higgs.vegbot.command.argument;

public class SentenceArgument extends Argument {

    public SentenceArgument(String key) {
        super(key);
    }

    @Override
    public String formatInputIfValid(String rawInput) {
        return rawInput;
    }
}
