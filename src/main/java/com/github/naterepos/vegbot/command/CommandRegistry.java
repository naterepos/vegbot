package com.github.naterepos.vegbot.command;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.command.implementation.*;
import com.github.naterepos.vegbot.resources.Permissions;
import com.github.naterepos.vegbot.user.VegUser;
import com.github.naterepos.vegbot.command.argument.Args;
import com.github.naterepos.vegbot.command.argument.Argument;
import com.github.naterepos.vegbot.command.argument.OptionalArgument;
import com.github.naterepos.vegbot.command.argument.SentenceArgument;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandRegistry implements Accessor {

    private static CommandRegistry instance;
    private Map<String, Command> commandKeyMap;
    private String key;

    public static CommandRegistry getOrCreate() {
        if(instance == null) {
            instance = new CommandRegistry();
            instance.key = instance.settings().getCommandKey();
            instance.commandKeyMap = new HashMap<>();
            instance.registerDefaultCommands();
        }
        return instance;
    }

    private void registerDefaultCommands() {
        registerCommand(Command.builder().keys("help", "info")
                .arguments(Args.optional(Args.STRING.named("command")))
                .information("Displays information about VegBot's commands")
                .usage(key + "help [<command>]")
                .executor(HelpCommand::new)
                .build());

        registerCommand(Command.builder().keys("profile")
                .arguments(Args.optional(Args.USER.named("user")))
                .information("Displays user profile")
                .usage(key + "profile [<user>]")
                .executor(ProfileCommand::new)
                .build());

        registerCommand(Command.builder().keys("addrole", "giverole")
                .arguments(Args.USER.named("user"), Args.ROLE.named("role"))
                .information("Adds a role to a given user")
                .usage(key + "addrole <user> <role>")
                .permission(Permissions.PERMISSION_EDIT)
                .executor(AddRoleCommand::new)
                .build());

        registerCommand(Command.builder().keys("removerole", "delrole", "deleterole")
                .arguments(Args.USER.named("user"), Args.ROLE.named("role"))
                .information("Removes a role from a given user")
                .usage(key + "removerole <user> <role>")
                .permission(Permissions.PERMISSION_EDIT)
                .executor(RemoveRoleCommand::new)
                .build());

        registerCommand(Command.builder().keys("argument", "arguments", "args", "arg")
                .arguments(Args.optional(Args.STRING.named("argument")))
                .information("Responses to common anti-vegan argument")
                .usage(key + "argument [<topic>]")
                .permission(Permissions.ARGUMENT)
                .executor(ArgumentCommand::new)
                .build());

        registerCommand(Command.builder().keys("leaderboards", "top", "leaderboards")
                .information("Show the top players by activity")
                .usage(key + "leaderboards")
                .executor(LeaderboardCommand::new)
                .build());

        registerCommand(Command.builder().keys("purge", "deleteall", "removeall")
                .information("Purges all non-pinned messages in a channel")
                .usage(key + "purge")
                .permission(Permissions.PURGE)
                .executor(PurgeCommand::new)
                .build());
    }

    public void registerCommand(Command command) {
        for(String key : command.getKeys()) {
            commandKeyMap.put(key, command);
        }
    }

    public Optional<Command> getCommand(String key) {
        return Optional.ofNullable(commandKeyMap.getOrDefault(key, null));
    }

    public Collection<Command> getAllCommands() {
        return commandKeyMap.values();
    }

    public Pair<String, CommandContext> parseArguments(VegUser source, Command command, String[] givenArguments, MessageChannel channel, Message originalMessage) {
        if(command.getArguments().isEmpty() && givenArguments.length == 0) {
            return new ImmutablePair<>(null, new CommandContext(source, channel, originalMessage));
        }

        if(!source.hasPermission(command.getPermission())) {
            return new ImmutablePair<>("You do not have permissions to use that command!", null);
        }

        CommandContext context = new CommandContext(source, channel, originalMessage);
        int argumentCount = 0;
        String[] keys = new String[command.getArguments().keySet().size()];
        Object[] values = new Object[keys.length];

        if(givenArguments.length < keys.length) {
            int amountOptional = 0;
            for (Argument value : command.getArguments().values()) {
                if(value instanceof OptionalArgument) {
                    amountOptional++;
                }
            }

            if(command.getArguments().size() - amountOptional > givenArguments.length) {
                return new ImmutablePair<>("Incorrect number of arguments. Use " + settings().getCommandKey() + "help for more information!", null);
            }
        }

        for(Map.Entry<String, Argument> entry : command.getArguments().entrySet()) {
            String key = entry.getKey();
            Argument expected = entry.getValue();

            if(expected instanceof OptionalArgument && givenArguments.length < command.getArguments().size()) {
                break;
            }

            String rawInput = givenArguments[argumentCount];
            Object formatted = expected.formatInputIfValid(rawInput);

            if (formatted != null) {
                keys[argumentCount] = key;

                if (expected instanceof SentenceArgument) {
                    StringBuilder string = new StringBuilder();
                    for (int i = argumentCount; i < givenArguments.length; i++) {
                        string.append(givenArguments[i]);
                        if (i < givenArguments.length - 1) {
                            string.append(" ");
                        }
                    }
                    values[argumentCount] = string.toString();
                    context.fillValues(keys, values);
                    return new ImmutablePair<>(null, context);
                } else {
                    values[argumentCount] = formatted;
                }
            } else if(!(expected instanceof OptionalArgument)) {
                return new ImmutablePair<>("Invalid arguments. Use " + key + "help for more information!", null);
            }

            argumentCount++;
            if(argumentCount >= givenArguments.length) {
                break;
            }
        }

        context.fillValues(keys, values);
        return new ImmutablePair<>(null, context);
    }
}
