package com.github.higgs.vegbot.events;

import com.github.higgs.vegbot.VegBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ConnectionEvents extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        Guild guild = VegBot.getJDA().getGuildById(VegBot.<Long>getSetting("GuildID"));
        if(guild != null && guild.getIdLong() == e.getGuild().getIdLong()) {
            VegBot.getUsers().addMemberToCache(e.getMember());
        }
    }
}
