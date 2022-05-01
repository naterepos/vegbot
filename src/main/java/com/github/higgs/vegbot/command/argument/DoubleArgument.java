package com.github.higgs.vegbot.command.argument;

public class DoubleArgument extends Argument {

    public DoubleArgument(String key) {
        super(key);
    }

    @Override
    public Double formatInputIfValid(String rawInput) {
        try {
            return Double.parseDouble(rawInput);
        } catch(NumberFormatException e) {
            return null;
        }
    }
}

