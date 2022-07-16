package com.github.naterepos.vegbot.events;

import com.github.naterepos.vegbot.Accessor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ConnectionEvents extends ListenerAdapter implements Accessor {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        Guild guild = guild();
        if(guild != null && guild.getIdLong() == e.getGuild().getIdLong()) {
            users().addMemberToCache(e.getMember());
        }
    }
}
