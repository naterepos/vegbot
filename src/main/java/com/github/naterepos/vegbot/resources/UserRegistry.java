package com.github.naterepos.vegbot.resources;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.user.VegUser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UserRegistry implements Accessor {

    private static UserRegistry instance;
    private List<Member> memberCache;

    public static UserRegistry getOrCreate() {
        if(instance == null) {
            instance = new UserRegistry();
        }
        return instance;
    }

    private UserRegistry() {
        if(guild() != null) {
            memberCache = guild().loadMembers().get();
        }
    }

    public Optional<VegUser> getUser(long id) {
        final User user = jda().retrieveUserById(id).complete();
        if(guild() != null) {
            final Member member = guild().retrieveMember(user).complete();
            return Optional.of(new VegUser(user, member));
        }
        return Optional.empty();
    }

    public CompletableFuture<Set<VegUser>> getTopLeaderboard() {
        return CompletableFuture.supplyAsync(() -> {
            Set<VegUser> top = new TreeSet<>(Comparator.naturalOrder());
            for (Member member : getAllMembers()) {
                getUser(member.getIdLong()).ifPresent(top::add);
            }
            return top;
        });
    }

    public List<Member> getAllMembers() {
        if(guild() != null) {
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
        data().tryCreateProfile(member);
    }
}
