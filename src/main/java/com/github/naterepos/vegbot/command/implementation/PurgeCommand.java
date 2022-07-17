package com.github.naterepos.vegbot.command.implementation;

import com.github.naterepos.vegbot.command.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PurgeCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context) {
        AtomicInteger messages = new AtomicInteger();
        context.getChannel().sendMessage("Purging \"" + context.getChannel().getName() + "\". This may take a moment...").queue();
        List<Message> delete = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        AtomicBoolean isFirstRequest = new AtomicBoolean(true);
        AtomicReference<Message> lastMessage = new AtomicReference<>();
        List<Message> pinned = context.getChannel().retrievePinnedMessages().complete();

        while(!isDone.get()) {
            if(isFirstRequest.get()) {
                MessageHistory history = context.getChannel().getHistoryFromBeginning(100).complete();
                if(history.getRetrievedHistory().size() > pinned.size()) {
                    for(int i = 0; i < history.getRetrievedHistory().size(); i++) {
                        Message message = history.getRetrievedHistory().get(i);
                        if(i == 0) {
                            lastMessage.set(message);
                            isFirstRequest.set(false);
                        }
                        if(!message.isPinned()) {
                            delete.add(message);
                        }
                    }
                } else {
                    messages.set(delete.size());
                    isDone.set(true);
                }
            } else {
                MessageHistory history = context.getChannel().getHistoryAfter(lastMessage.get(), 100).complete();
                if(history.getRetrievedHistory().size() > pinned.size()) {
                    for (Message message : history.getRetrievedHistory()) {
                        if(!message.isPinned()) {
                            delete.add(message);
                        }
                        lastMessage.set(message);
                    }
                } else {
                    messages.set(delete.size());
                    isDone.set(true);
                }
            }
        }
        List<CompletableFuture<Void>> completed = context.getChannel().purgeMessages(delete);
        for(int i = 0; i < completed.size(); i++) {
            completed.get(i).join();
            if(i == (completed.size() - 1)) {
                return CommandResult.temporary(CommandResults.SUCCESS, context.getChannel().getName() + " has had " + messages + " removed!", 4);
            }
        }
        return CommandResult.temporary(CommandResults.SUCCESS, context.getChannel().getName() + " has had " + messages + " removed!", 4);
    }
}
