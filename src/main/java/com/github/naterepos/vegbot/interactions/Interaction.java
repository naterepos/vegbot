package com.github.naterepos.vegbot.interactions;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.user.VegUser;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Interaction implements Accessor {

    private Map<String, Action> reactionMap;
    private boolean isLive = true;
    private String currentEmote;
    private final VegUser reactor;
    private final Message origin;

    public Interaction(VegUser reactor, Message origin) {
        this.reactor = reactor;
        this.origin = origin;
        this.reactionMap = new HashMap<>();
    }

    public void addOption(String emote, Action action) {
        reactionMap.put(emote, action);
        origin.addReaction(emote).queue();
    }

    public void addOptions(Map<String, Action> reactionMap) {
        this.reactionMap = reactionMap;
    }

    public Message getOriginalMessage() {
        return origin;
    }

    public void registerReaction(VegUser reactor, String emote) {
        if(isLive && this.reactor.getUser().getId().equals(reactor.getUser().getId()) && reactionMap.containsKey(emote)) {
            currentEmote = emote;
            reactionMap.get(emote).runReactionAction();
        }
    }

    public void registerTextInput(String input) {
        if(currentEmote != null) {
            reactionMap.get(currentEmote).runTextAction(input);
            currentEmote = null;
        }
    }

    public void finishAndCleanup(boolean deleteMessage, boolean completelyWipe) {
        this.isLive = false;
        if(deleteMessage) {
            if(completelyWipe) {
                origin.getChannel().getHistoryAfter(origin.getId(), 30).queueAfter(4, TimeUnit.SECONDS, messageHistory -> messageHistory.getRetrievedHistory().forEach(message -> {
                    if(message.getAuthor().getId().equals(reactor.getUser().getId()) || message.getAuthor().getId().equals(origin.getAuthor().getId())) {
                        message.delete().queue();
                    }
                }));
            }
            origin.delete().queue();
        }
        interactions().removeUser(reactor.getUser().getId());
    }

    public boolean hasValidTextAction() {
        return currentEmote != null && reactionMap.get(currentEmote).textAction != Action.DEFAULT_TEXT_ACTION;
    }

    public static class Action {
        public static final Consumer<String> DEFAULT_TEXT_ACTION = str -> {};
        public static final Runnable DEFAULT_REACT_ACTION = () -> {};

        private final Consumer<String> textAction;
        private final Runnable reactAction;

        public Action(Consumer<String> textAction, Runnable reactAction) {
            this.textAction = textAction;
            this.reactAction = reactAction;
        }

        public void runReactionAction() {
            if(reactAction != DEFAULT_REACT_ACTION) {
                reactAction.run();
            }
        }

        public void runTextAction(String input) {
            if(textAction != DEFAULT_TEXT_ACTION) {
                textAction.accept(input);
            }
        }
    }
}
