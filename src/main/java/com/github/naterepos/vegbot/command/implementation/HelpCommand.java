package com.github.naterepos.vegbot.command.implementation;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.command.Command;
import com.github.naterepos.vegbot.command.CommandContext;
import com.github.naterepos.vegbot.command.CommandExecutor;
import com.github.naterepos.vegbot.command.CommandResult;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class HelpCommand implements CommandExecutor, Accessor {

    @Override
    public CommandResult execute(CommandContext context) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Command List")
                .setAuthor("VegBot", "https://cdn.discordapp.com/avatars/944634672128868464/fd66f15284ce8e9ae21a3c180066279e.webp", "https://cdn.discordapp.com/avatars/944634672128868464/fd66f15284ce8e9ae21a3c180066279e.webp")
                .setColor(Color.CYAN);

        Set<Command> previous = new HashSet<>();

        for (Command command : commands().getAllCommands()) {
            if (context.getSource().hasPermission(command.getPermission()) && !previous.contains(command)) {
                embedBuilder.addField(command.getUsage(), command.getInformation(), false);
            }
            previous.add(command);
        }
        context.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

        return CommandResult.empty();
    }
}
