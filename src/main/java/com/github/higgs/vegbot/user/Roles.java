package com.github.higgs.vegbot.user;

import com.github.higgs.vegbot.resources.Permissions;

import java.util.*;

public class Roles {

    public static final Role CURIOUS = new Role("CURIOUS", "Curious", 0, new HashMap<>(), Permissions.EVERYONE);
    public static final Role MEMBER, MEDIA_COORDINATOR, OUTREACH_COORDINATOR, TREASURY_COORDINATOR, STAFF_COORDINATOR, ORGANIZER;
    private static final List<Role> roles;

    static {
        Map<String, Role> children = new HashMap<>();
        children.put(CURIOUS.getName(), CURIOUS);
        MEMBER = new Role("MEMBER","Member", 1, children, Permissions.EVERYONE);

        children = new HashMap<>();
        children.put(MEMBER.getName(), MEMBER);
        MEDIA_COORDINATOR = new Role("MEDIA_COORDINATOR", "Media Coordinator", 50, children, Permissions.COORDINATOR);
        OUTREACH_COORDINATOR = new Role("OUTREACH_COORDINATOR", "Outreach Coordinator", 50, children, Permissions.COORDINATOR);
        TREASURY_COORDINATOR = new Role("TREASURY_COORDINATOR", "Treasury Coordinator", 50, children, Permissions.COORDINATOR);
        STAFF_COORDINATOR = new Role("STAFF_COORDINATOR", "Staff Coordinator", 50, children, Permissions.COORDINATOR);

        children = new HashMap<>();
        children.put(MEDIA_COORDINATOR.getID(), MEDIA_COORDINATOR);
        children.put(OUTREACH_COORDINATOR.getID(), OUTREACH_COORDINATOR);
        children.put(TREASURY_COORDINATOR.getID(), TREASURY_COORDINATOR);
        children.put(STAFF_COORDINATOR.getID(), STAFF_COORDINATOR);

        ORGANIZER = new Role("ORGANIZER", "Organizer", 100, children, Permissions.ADMINISTRATION);

        roles = Arrays.asList(CURIOUS, MEMBER, MEDIA_COORDINATOR, OUTREACH_COORDINATOR, TREASURY_COORDINATOR, STAFF_COORDINATOR, ORGANIZER);
    }

    public static Role getRoleFromString(String roleString) {
        for(Role role : roles) {
            if(role.getID().equalsIgnoreCase(roleString)) {
                return role;
            }
        }
        return CURIOUS;
    }

    public static Optional<Role> tryGetRoleFromString(String roleString) {
        for(Role role : roles) {
            if(role.getID().equalsIgnoreCase(roleString)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }
}
