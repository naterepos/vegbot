package com.github.higgs.vegbot.command.argument;

public enum Args {

    STRING, SENTENCE, INTEGER, DOUBLE, BOOLEAN, USER, ROLE;

    public Argument named(String key) {
        if(this == SENTENCE) {
            return new SentenceArgument(key);
        } else if(this == INTEGER) {
            return new IntegerArgument(key);
        } else if(this == DOUBLE) {
            return new DoubleArgument(key);
        } else if(this == BOOLEAN) {
            return new BooleanArgument(key);
        } else if(this == USER) {
            return new UserArgument(key);
        } else if(this == ROLE) {
            return new RoleArgument(key);
        } else {
            return new WordArgument(key);
        }
    }

    public static Argument optional(Argument argument) {
        return new OptionalArgument(argument.getKey(), argument);
    }
}
