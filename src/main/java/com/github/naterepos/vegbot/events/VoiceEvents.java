package com.github.naterepos.vegbot.events;

import com.github.naterepos.vegbot.Accessor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VoiceEvents extends ListenerAdapter implements Accessor {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent e) {
        if(!e.getMember().getUser().isBot() && settings().isAllowedPointChannel(e.getChannelJoined().getId())) {
            voiceHolder().setUserJoined(e.getMember().getId());
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent e) {
        if(!e.getMember().getUser().isBot()) {
            users().getUser(e.getMember().getIdLong()).ifPresent(user -> user.addPoints(PointActionTypes.SPENT_TIME_IN_VC, voiceHolder().getMinutesInVC(e.getMember().getId())));
            voiceHolder().setUserLeft(e.getMember().getId());
        }
    }
}
