package com.github.higgs.vegbot.user;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class Role implements Comparable<Role> {

    private final int weight;
    private final String ID, name;
    private final Map<String, Role> children;
    private final Map<String, Boolean> permissions;

    public Role(String ID, String name, int weight, Map<String, Role> children, Map<String, Boolean> permissions) {
        this.ID = ID;
        this.name = name;
        this.children = children;
        this.permissions = permissions;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public int getWeight() {
        return weight;
    }

    public boolean hasPermission(String permission) {
        boolean hasPermission = permissions.getOrDefault(permission, false);
        if(hasPermission) {
            return true;
        } else {
            if(children != null) {
                for(Role child : children.values()) {
                    return child.hasPermission(permission);
                }
            }
        }
        return false;
    }

    public Set<Map.Entry<String, Role>> getChildren() {
        return children.entrySet();
    }

    /**
     * @param other role to compare
     * @return positive if this role has a higher weight than the parameter, 0 if equal, and negative if less
     */
    @Override
    public int compareTo(@NotNull Role other) {
        return this.weight - other.weight;
    }
}
