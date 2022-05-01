package com.github.higgs.vegbot.command;

import com.github.higgs.vegbot.command.argument.Argument;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Supplier;

public class Command {

    private final Supplier<CommandExecutor> executor;
    private final LinkedHashSet<String> keys;
    private final String  information, usage, permission;
    private final Map<String, Argument> args;

    private Command(LinkedHashSet<String> keys, Supplier<CommandExecutor> executor, Argument[] args, String information, String usage, String permission) {
        this.keys = keys;
        this.information = information;
        this.executor = executor;
        this.args = new LinkedHashMap<>();
        this.usage = usage;
        this.permission = permission;
        for(Argument arg : args) {
            this.args.put(arg.getKey(), arg);
        }
    }

    public LinkedHashSet<String> getKeys() {
        return keys;
    }

    public Map<String, Argument> getArguments() {
        return args;
    }

    public String getInformation() {
        return information;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public CommandResult execute(CommandContext context) {
        return executor.get().execute(context);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final LinkedHashSet<String> keys = new LinkedHashSet<>();
        private String information, usage, permission;
        private Supplier<CommandExecutor> executor;
        private Argument[] args;

        public Builder keys(String... keys) {
            this.keys.addAll(Arrays.asList(keys));
            return this;
        }

        public Builder executor(Supplier<CommandExecutor> executor) {
            this.executor = executor;
            return this;
        }

        public Builder arguments(Argument... args) {
            this.args = args;
            return this;
        }

        public Builder information(String information) {
            this.information = information;
            return this;
        }

        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        public Builder permission(String permission) {
            this.permission = permission;
            return this;
        }

        public Command build() {
            return new Command(keys, executor, args == null ? new Argument[0] : args, information, usage, permission);
        }
    }
}
