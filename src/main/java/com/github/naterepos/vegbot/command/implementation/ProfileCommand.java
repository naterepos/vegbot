package com.github.naterepos.vegbot.command.implementation;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.command.CommandResults;
import com.github.naterepos.vegbot.resources.Permissions;
import com.github.naterepos.vegbot.user.VegUser;
import com.github.naterepos.vegbot.command.CommandContext;
import com.github.naterepos.vegbot.command.CommandExecutor;
import com.github.naterepos.vegbot.command.CommandResult;
import com.github.naterepos.vegbot.interactions.Interaction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.Optional;

public class ProfileCommand implements CommandExecutor, Accessor {

    private VegUser source;
    private VegUser vegUser;
    private String nounPrefix = "your", nounPrefixCapitalized = "Your";
    private Message interactionMessage, selectMessage;
    private Interaction modificationTrigger;

    @Override
    public CommandResult execute(CommandContext context) {
        Optional<VegUser> user = context.requestArgument("user");
        this.vegUser = user.orElse(context.getSource());
        this.source = context.getSource();
        if(!vegUser.getUser().getId().equals(source.getUser().getId())) {
            if(!source.hasPermission(Permissions.PROFILE_MODIFY_OTHERS)) {
                return CommandResult.of(CommandResults.ERROR, "You do not have permissions to use that command!");
            }
            nounPrefix = vegUser.getUser().getName() + "'s";
            nounPrefixCapitalized = nounPrefix;
        }

        MessageEmbed embedBuilder = new EmbedBuilder()
                .setAuthor(vegUser.getUser().getAsTag(), vegUser.getUser().getAvatarUrl(), vegUser.getUser().getAvatarUrl())
                .addField("Name", vegUser.getProfile().getName(), true)
                .addField("Pronouns", vegUser.getProfile().getPronouns(), true)
                .addField("Role", vegUser.getTopRole().getName(), true)
                .addField("Time as a Vegan", vegUser.getProfile().getFormattedTimeVegan(), true)
                .addField("Points", String.valueOf(vegUser.getPoints()), true)
                .setColor(Color.CYAN)
                .build();

        context.getChannel().sendMessageEmbeds(embedBuilder).queue(message -> {
            interactionMessage = message;
            modificationTrigger = new Interaction(source, message);

            Interaction.Action modificationAction = new Interaction.Action(Interaction.Action.DEFAULT_TEXT_ACTION, () -> {
                modificationTrigger.finishAndCleanup(true, false);
                MessageEmbed selectEmbed = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .addField("Profile Editor",
                                "🔤  -  Modify Name\n\n" +
                                        "♀ ️ -  Modify Pronouns\n\n" +
                                        "📅  -  Modify Months Vegan", false)
                        .build();

                interactionMessage.getChannel().sendMessageEmbeds(selectEmbed).queue(selectMessage -> {
                    this.selectMessage = selectMessage;

                    Interaction modifyProfile = new Interaction(source, selectMessage);
                    modifyProfile.addOption("🔤", new Interaction.Action(this::modifyName, () ->
                            selectMessage.getChannel().sendMessage("Please enter " + nounPrefix + " preferred name: ").queue()));
                    modifyProfile.addOption("♀", new Interaction.Action(this::modifyPronouns, () ->
                            selectMessage.getChannel().sendMessage("Please enter " + nounPrefix + " preferred pronouns: ").queue()));
                    modifyProfile.addOption("📅", new Interaction.Action(this::modifyMonthsVegan, () ->
                            selectMessage.getChannel().sendMessage("Please enter " + nounPrefix + " time as a vegan (months): ").queue()));
                    interactions().addUser(source.getUser().getId(), modifyProfile);
                });
            });

            modificationTrigger.addOption("✍️", modificationAction);
            interactions().addUser(source.getUser().getId(), modificationTrigger);
        });

        return CommandResult.empty();
    }

    private void modifyPronouns(String input) {
        if(input.length() <= 20) {
            vegUser.getProfile().setPronouns(input);
            selectMessage.getChannel().sendMessage(nounPrefixCapitalized + " pronouns are now \"" + input + "\"").queue();
        } else {
            selectMessage.getChannel().sendMessage("Name cannot exceed 30 characters!").queue();
        }
    }

    private void modifyMonthsVegan(String input) {
        try {
            int months = Integer.parseInt(input);
            if(months >= 0) {
                vegUser.getProfile().setMonthsVegan(months);
                selectMessage.getChannel().sendMessage(nounPrefixCapitalized + " time logged as a vegan is now \"" + vegUser.getProfile().getFormattedTimeVegan() + "\"").queue();
                return;
            }
        } catch(NumberFormatException ignored) {}
        selectMessage.getChannel().sendMessage("Amount of time must be positive!").queue();
    }

    private void modifyName(String input) {
        if(input.length() <= 30) {
            vegUser.getProfile().setRealName(input);
            selectMessage.getChannel().sendMessage(nounPrefixCapitalized + " name is now \"" + input + "\"").queue();
        } else {
            selectMessage.getChannel().sendMessage("Name cannot exceed 30 characters!").queue();
        }
    }
}
