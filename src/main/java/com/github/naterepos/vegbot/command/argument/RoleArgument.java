package com.github.naterepos.vegbot.command.argument;

import com.github.naterepos.vegbot.user.Role;
import com.github.naterepos.vegbot.user.Roles;

public class RoleArgument extends Argument{

    public RoleArgument(String key) {
        super(key);
    }

    @Override
    public Role formatInputIfValid(String rawInput) {
        return Roles.tryGetRoleFromString(rawInput).orElse(null);
    }
}
