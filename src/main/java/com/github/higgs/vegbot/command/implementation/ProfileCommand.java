package com.github.higgs.vegbot.command.implementation;

import com.github.higgs.vegbot.VegBot;
import com.github.higgs.vegbot.command.CommandResults;
import com.github.higgs.vegbot.resources.Permissions;
import com.github.higgs.vegbot.user.VegUser;
import com.github.higgs.vegbot.command.CommandContext;
import com.github.higgs.vegbot.command.CommandExecutor;
import com.github.higgs.vegbot.command.CommandResult;
import com.github.higgs.vegbot.interactions.Interaction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.Optional;

public class ProfileCommand implements CommandExecutor {

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
                .addField("Name", vegUser.getInfo().getName(), true)
                .addField("Pronouns", vegUser.getInfo().getPronouns(), true)
                .addField("Role", vegUser.getTopRole().getName(), true)
                .addField("Time as a Vegan", vegUser.getInfo().getFormattedTimeVegan(), true)
                .setColor(Color.CYAN)
                .build();

        context.getChannel().sendMessageEmbeds(embedBuilder).queue(message -> {
            interactionMessage = message;
            Interaction.Action modificationAction = new Interaction.Action(Interaction.Action.DEFAULT_TEXT_ACTION, this::modify);
            modificationTrigger = new Interaction(source, message);
            modificationTrigger.addOption("✍️", modificationAction);
            VegBot.getInteractions().addUser(source.getUser().getId(), modificationTrigger);
        });

        return CommandResult.empty();
    }

    private void modify() {
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
            modifyProfile.addOption("🔤", new Interaction.Action(this::modifyName, () -> {
                selectMessage.getChannel().sendMessage("Please enter " + nounPrefix + " preferred name: ").queue();
            }));
            modifyProfile.addOption("♀", new Interaction.Action(this::modifyPronouns, () -> {
                selectMessage.getChannel().sendMessage("Please enter " + nounPrefix + " preferred pronouns: ").queue();
            }));
            modifyProfile.addOption("📅", new Interaction.Action(this::modifyMonthsVegan, () -> {
                selectMessage.getChannel().sendMessage("Please enter " + nounPrefix + " time as a vegan (months): ").queue();
            }));
            VegBot.getInteractions().addUser(source.getUser().getId(), modifyProfile);
        });
    }

    private void modifyPronouns(String input) {
        if(input.length() <= 20) {
            vegUser.getInfo().setPronouns(input);
            selectMessage.getChannel().sendMessage(nounPrefixCapitalized + " pronouns are now \"" + input + "\"").queue();
        } else {
            selectMessage.getChannel().sendMessage("Name cannot exceed 30 characters!").queue();
        }
    }

    private void modifyMonthsVegan(String input) {
        try {
            int months = Integer.parseInt(input);
            if(months >= 0) {
                vegUser.getInfo().setMonthsVegan(months);
                selectMessage.getChannel().sendMessage(nounPrefixCapitalized + " time logged as a vegan is now \"" + vegUser.getInfo().getFormattedTimeVegan() + "\"").queue();
                return;
            }
        } catch(NumberFormatException ignored) {}
        selectMessage.getChannel().sendMessage("Amount of time must be positive!").queue();
    }

    private void modifyName(String input) {
        if(input.length() <= 30) {
            vegUser.getInfo().setRealName(input);
            selectMessage.getChannel().sendMessage(nounPrefixCapitalized + " name is now \"" + input + "\"").queue();
        } else {
            selectMessage.getChannel().sendMessage("Name cannot exceed 30 characters!").queue();
        }
    }
}
