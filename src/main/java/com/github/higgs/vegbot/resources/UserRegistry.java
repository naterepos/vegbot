package com.github.higgs.vegbot.resources;

import com.github.higgs.vegbot.VegBot;
import com.github.higgs.vegbot.user.VegUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRegistry {

    private static UserRegistry instance;
    private List<Member> memberCache;

    public static UserRegistry getOrCreate() {
        if(instance == null) {
            instance = new UserRegistry();
        }
        return instance;
    }

    private UserRegistry() {
        Guild guild = VegBot.getJDA().getGuildById(VegBot.<Long>getSetting("GuildID").orElse(0L));
        if(guild != null) {
            memberCache = guild.loadMembers().get();
        }
    }

    public Optional<VegUser> getUser(long id) {
        final User user = VegBot.getJDA().retrieveUserById(id).complete();
        Guild guild = VegBot.getJDA().getGuildById(VegBot.<Long>getSetting("GuildID").orElse(0L));
        if(guild != null) {
            final Member member = guild.retrieveMember(user).complete();
            return Optional.of(new VegUser(user, member));
        }
        return Optional.empty();
    }

    public List<Member> getAllMembers() {
        Guild guild = VegBot.getJDA().getGuildById(VegBot.<Long>getSetting("GuildID").orElse(0L));
        if(guild != null) {
            return instance.memberCache;
        }
        return new ArrayList<>();
    }

    public Optional<VegUser> getUser(String taggedId) {
        if(taggedId.startsWith("<")) {
            long id = Long.parseLong(taggedId.substring(2, taggedId.length() - 1));
            return getUser(id);
        } else {
            try {
                return getUser(Long.parseLong(taggedId));
            } catch(NumberFormatException ignored) {}
        }
        return Optional.empty();
    }

    public void addMemberToCache(Member member) {
        memberCache.add(member);
        VegBot.getSQL().tryCreateProfile(member);
    }
}
