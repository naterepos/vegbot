package com.github.higgs.vegbot.user;

import com.github.higgs.vegbot.VegBot;
import com.github.higgs.vegbot.resources.MySQL;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VegUser {

    private final User user;
    private final Member member;
    private final UserInfo info;
    private final TreeMap<String, Role> roles;

    public VegUser(User user, Member member) {
        this.user = user;
        this.member = member;
        this.info = new UserInfo(this);
        this.roles = VegBot.getSQL().getRoles(this);
    }

    public boolean hasPermission(String permission) {
        if(permission == null) {
            return true;
        }
        for (Role role : roles.values()) {
            if(role.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    public void removeRole(Role role) {
        VegBot.getSQL().removeRole(user.getId(), role);
        // Cannot remove classically since tree map does not allow null values
        roles.entrySet().forEach(entry -> {
            if(entry.getKey().equals(role.getID())) {
                roles.remove(role.getID());
            }
        });
        List<net.dv8tion.jda.api.entities.Role> possibleRole = VegBot.getGuild().getRolesByName(role.getID(), true);
        if(possibleRole.size() != 0) {
            VegBot.getGuild().removeRoleFromMember(member, possibleRole.get(0)).queue();
        }
    }

    public void addRole(Role role) {
        VegBot.getSQL().addRole(user.getId(), role);
        roles.put(role.getID(), role);

        List<net.dv8tion.jda.api.entities.Role> possibleRole = VegBot.getGuild().getRolesByName(role.getID(), true);
        if(possibleRole.size() != 0) {
            VegBot.getGuild().addRoleToMember(member, possibleRole.get(0)).queue();
        }

        role.getChildren().forEach(child -> {
            // Discord aesthetic cleanup
            List<net.dv8tion.jda.api.entities.Role> rolesByName = VegBot.getGuild().getRolesByName(child.getKey(), true);
            if(rolesByName.size() != 0) {
                VegBot.getGuild().removeRoleFromMember(member, rolesByName.get(0)).queue();
            }
        });
    }

    public Role getTopRole() {
        return roles.lastEntry().getValue();
    }

    public Member getMember() {
        return member;
    }

    public User getUser() {
        return user;
    }

    public UserInfo getInfo() {
        return info;
    }

    public class UserInfo {
        private String realName, pronouns;
        private int monthsVegan;

        public UserInfo(VegUser user) {
            Map<String, String> info = VegBot.getSQL().getUserInfo(user.getUser().getId());
            realName = info.get("NAME");
            pronouns = info.get("PRONOUNS");
            monthsVegan = Integer.parseInt(info.get("MONTHS_VEGAN"));
        }

        public String getName() {
            return realName;
        }

        public String getPronouns() {
            return pronouns;
        }

        public int getMonthsVegan() {
            return monthsVegan;
        }

        public String getFormattedTimeVegan() {
            int years = monthsVegan / 12;
            int months = monthsVegan % 12;
            if(years == 0 && months == 0) {
                return "0 months";
            } else if(years == 0 ) {
                return (months == 1 ? months + " month" : months + " months");
            } else {
                return years + (years == 1 ? " year, " : " years, ") + months + (months == 1 ? " month" : " months");
            }
        }

        public void setRealName(String realName) {
            this.realName = realName;
            MySQL.getOrCreate().setRealName(VegUser.this.user.getId(), realName);
        }

        public void setPronouns(String pronouns) {
            this.pronouns = pronouns;
            MySQL.getOrCreate().setPronouns(VegUser.this.user.getId(), pronouns);
        }

        public void setMonthsVegan(int monthsVegan) {
            this.monthsVegan = monthsVegan;
            MySQL.getOrCreate().setMonthsVegan(VegUser.this.user.getId(), monthsVegan);
        }
    }
}
