package com.github.naterepos.vegbot.user;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.events.PointActionTypes;
import com.github.naterepos.vegbot.resources.MySQL;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VegUser implements Accessor {

    private final User user;
    private int points;
    private final Member member;
    private final Profile info;
    private final TreeMap<String, Role> roles;

    public VegUser(User user, Member member) {
        this.user = user;
        this.member = member;
        this.points = data().getPoints(user.getId());
        this.info = new Profile(this);
        this.roles = data().getRoles(this);
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(PointActionTypes type, int amount) {
        points += type.getWorth(amount);
        MySQL.getOrCreate().setPoints(user.getId(), points);
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
        data().removeRole(user.getId(), role);
        // Cannot remove classically since tree map does not allow null values
        for (Map.Entry<String, Role> entry : roles.entrySet()) {
            if (entry.getKey().equals(role.getID())) {
                roles.remove(role.getID());
            }
        }
        List<net.dv8tion.jda.api.entities.Role> possibleRole = guild().getRolesByName(role.getID(), true);
        if(possibleRole.size() != 0) {
            guild().removeRoleFromMember(member, possibleRole.get(0)).queue();
        }
    }

    public void addRole(Role role) {
        data().addRole(user.getId(), role);
        roles.put(role.getID(), role);

        List<net.dv8tion.jda.api.entities.Role> possibleRole = guild().getRolesByName(role.getID(), true);
        if(possibleRole.size() != 0) {
            guild().addRoleToMember(member, possibleRole.get(0)).queue();
        }

        for (Map.Entry<String, Role> child : role.getChildren()) {
            // Discord aesthetic cleanup
            List<net.dv8tion.jda.api.entities.Role> rolesByName = guild().getRolesByName(child.getKey(), true);
            if (rolesByName.size() != 0) {
                guild().removeRoleFromMember(member, rolesByName.get(0)).queue();
            }
        }
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

    public Profile getProfile() {
        return info;
    }

    public class Profile {
        private String realName, pronouns;
        private int monthsVegan;

        public Profile(VegUser user) {
            Map<String, String> info = data().getUserInfo(user.getUser().getId());
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
            MySQL.getOrCreate().setRealName(user.getId(), realName);
        }

        public void setPronouns(String pronouns) {
            this.pronouns = pronouns;
            MySQL.getOrCreate().setPronouns(user.getId(), pronouns);
        }

        public void setMonthsVegan(int monthsVegan) {
            this.monthsVegan = monthsVegan;
            MySQL.getOrCreate().setMonthsVegan(user.getId(), monthsVegan);
        }
    }
}
