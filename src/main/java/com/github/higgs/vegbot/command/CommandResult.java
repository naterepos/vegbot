package com.github.higgs.vegbot.command;

public class CommandResult {

    private final String message;
    private final CommandResults result;
    private final int secondsBeforeDeletion;

    private CommandResult(CommandResults result, String message, int secondsBeforeDeletion) {
        this.result = result;
        this.message = message;
        this.secondsBeforeDeletion = secondsBeforeDeletion;
    }

    public String getMessage() {
        return message;
    }

    public CommandResults getResult() {
        return result;
    }

    public int getSecondsBeforeDeletion() {
        return secondsBeforeDeletion;
    }

    public static CommandResult empty() {
        return new CommandResult(CommandResults.SILENT, "", -1);
    }

    public static CommandResult of(CommandResults result, String message) {
        return new CommandResult(result, message, -1);
    }

    public static CommandResult temporary(CommandResults result, String message, int secondsBeforeDeletion) {
        return new CommandResult(result, message, secondsBeforeDeletion);
    }
}
