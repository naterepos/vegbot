package com.github.higgs.vegbot.command.argument;

public class IntegerArgument extends Argument {

    public IntegerArgument(String key) {
        super(key);
    }

    @Override
    public Integer formatInputIfValid(String rawInput) {
        try {
            return Integer.parseInt(rawInput);
        } catch(NumberFormatException e) {
            return null;
        }
    }
}
