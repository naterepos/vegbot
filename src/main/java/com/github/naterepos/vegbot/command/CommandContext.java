package com.github.naterepos.vegbot.command;

import com.github.naterepos.vegbot.user.VegUser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandContext {

    private final VegUser source;
    private final Map<String, Object> arguments;
    private final MessageChannel channel;
    private final Message originalMessage;

    public CommandContext(VegUser source, MessageChannel channel, Message originalMessage) {
        this.arguments = new HashMap<>();
        this.source = source;
        this.channel = channel;
        this.originalMessage = originalMessage;
    }

    public void fillValues(String[] keys, Object[] inputs) {
        for(int i = 0; i < keys.length; i++) {
            arguments.put(keys[i], inputs[i]);
        }
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public VegUser getSource() {
        return source;
    }

    public Message getOriginalMessage() {
        return originalMessage;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> requestArgument(String key) {
        Object object = arguments.getOrDefault(key, null);
        try {
            if(object != null) {
                return Optional.of((T) object);
            }
        } catch (ClassCastException ignored) {}
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgument(String key) {
        return (T) arguments.getOrDefault(key, null);
    }
}
