package com.github.naterepos.vegbot.resources;

import java.util.HashMap;
import java.util.Map;

public class Permissions {

    public static final String PROFILE_MODIFY_SELF = "profile.modify.self";
    public static final String PROFILE_MODIFY_OTHERS = "profile.modify.others";
    public static final String PERMISSION_EDIT = "permission.edit";
    public static final String PERMISSION_VIEW = "permission.view";
    public static final String LINKS_VIEW = "links.view";
    public static final String LINKS_EDIT = "links.edit";
    public static final String ARGUMENT = "argument";
    public static final String PURGE = "purge";

    public static final Map<String, Boolean> ADMINISTRATION;
    public static final Map<String, Boolean> COORDINATOR;
    public static final Map<String, Boolean> MEMBER;
    public static final Map<String, Boolean> EVERYONE;

    static {
        EVERYONE = new HashMap<>();
        EVERYONE.put(PROFILE_MODIFY_SELF, true);
        EVERYONE.put(LINKS_VIEW, true);
        EVERYONE.put(ARGUMENT, true);

        MEMBER = new HashMap<>();

        COORDINATOR = new HashMap<>();
        COORDINATOR.put(PROFILE_MODIFY_OTHERS, true);
        COORDINATOR.put(PERMISSION_VIEW, true);

        ADMINISTRATION = new HashMap<>();
        ADMINISTRATION.put(PERMISSION_EDIT, true);
        ADMINISTRATION.put(LINKS_EDIT, true);
        ADMINISTRATION.put(PURGE, true);
    }
}
