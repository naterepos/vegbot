package com.github.naterepos.vegbot.command.implementation;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.command.CommandContext;
import com.github.naterepos.vegbot.command.CommandExecutor;
import com.github.naterepos.vegbot.command.CommandResult;
import com.github.naterepos.vegbot.user.VegUser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class LeaderboardCommand implements CommandExecutor, Accessor {

    @Override
    public CommandResult execute(CommandContext context) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.CYAN);

        StringBuilder info = new StringBuilder();
        int place = 1;
        for (VegUser user : users().getTopLeaderboard().join()) {
            info.append("**").append(place).append("**: `").append(user.getUser().getName()).append("` - ").append(user.getPoints()).append(" points\n");
            place++;
        }

        embedBuilder.addField("Most Active Members", info.toString(), false);
        context.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

        return CommandResult.empty();
    }
}
