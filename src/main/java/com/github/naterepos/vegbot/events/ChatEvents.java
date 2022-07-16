package com.github.naterepos.vegbot.events;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.command.Command;
import com.github.naterepos.vegbot.command.CommandContext;
import com.github.naterepos.vegbot.command.CommandResult;
import com.github.naterepos.vegbot.command.CommandResults;
import com.github.naterepos.vegbot.interactions.Interaction;
import com.github.naterepos.vegbot.user.VegUser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ChatEvents extends ListenerAdapter implements Accessor {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if(e.isWebhookMessage() ||e.getAuthor().isBot()) return;

        if(e.getMessage().getContentRaw().startsWith(settings().getCommandKey())) {
            Optional<VegUser> userOpt = users().getUser(e.getAuthor().getIdLong());
            userOpt.ifPresent(user -> {
                String fullCommand = e.getMessage().getContentRaw().substring(1);
                String[] inputStrings = fullCommand.split(" ");
                String[] arguments;

                if (inputStrings.length > 1) {
                    arguments = fullCommand.substring(inputStrings[0].length() + 1).split(" ");
                } else {
                    arguments = new String[0];
                }

                Optional<Command> commandOpt = commands().getCommand(inputStrings[0]);
                if (commandOpt.isPresent()) {
                    Pair<String, CommandContext> pair = commands().parseArguments(user, commandOpt.get(), arguments, e.getChannel(), e.getMessage());
                    if (pair.getLeft() == null) {
                        CommandResult result = commandOpt.get().execute(pair.getRight());
                        if (result.getResult() != CommandResults.SILENT) {
                            e.getChannel().sendMessage(result.getMessage()).queue(feedback -> {
                                if (result.getSecondsBeforeDeletion() > 0) {
                                    feedback.delete().queueAfter(result.getSecondsBeforeDeletion(), TimeUnit.SECONDS);
                                }
                            });
                        }
                    } else {
                        e.getChannel().sendMessage(pair.getLeft()).queue(feedback -> feedback.delete().queueAfter(3, TimeUnit.SECONDS));
                    }
                } else {
                    e.getChannel().sendMessage("That command does not exist!").queue(feedback -> feedback.delete().queueAfter(3, TimeUnit.SECONDS));
                }
            });
        } else {
            Optional<VegUser> userOpt = users().getUser(e.getAuthor().getIdLong());
            Optional<Interaction> interactionOpt = userOpt.flatMap(user -> interactions().getActiveInteraction(user.getUser().getId()));
            if(interactionOpt.isPresent()) {
                Interaction interaction = interactionOpt.get();
                if(interaction.getOriginalMessage().getChannel().getId().equals(e.getChannel().getId()) && interaction.hasValidTextAction()) {
                    interaction.registerTextInput(e.getMessage().getContentRaw());
                    interaction.finishAndCleanup(true, true);
                }
            } else if(settings().isAllowedPointChannel(e.getChannel().getId())) {
                userOpt.ifPresent(user -> user.addPoints(PointActionTypes.SENT_CHAT, 1));
            }
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent e) {
        if(e.getUser() != null && !e.getUser().isBot()) {
            interactions().getActiveInteraction(e.getUserId()).ifPresent(interaction -> {
                if(interaction.getOriginalMessage().getId().equals(e.getMessageId()) && interaction.getOriginalMessage().getChannel().getId().equals(e.getChannel().getId())) {
                    e.retrieveMessage().complete().clearReactions().queue();
                    interaction.registerReaction(users().getUser(e.getUserId()).orElse(null), e.getReactionEmote().getEmoji());
                }
            });
        }
    }
}
