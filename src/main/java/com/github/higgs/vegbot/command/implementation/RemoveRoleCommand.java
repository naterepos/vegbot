package com.github.higgs.vegbot.command.implementation;

import com.github.higgs.vegbot.command.CommandContext;
import com.github.higgs.vegbot.command.CommandExecutor;
import com.github.higgs.vegbot.command.CommandResult;
import com.github.higgs.vegbot.command.CommandResults;
import com.github.higgs.vegbot.user.Role;
import com.github.higgs.vegbot.user.Roles;
import com.github.higgs.vegbot.user.VegUser;

public class RemoveRoleCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context) {
        VegUser target = context.getArgument("user");
        Role role = context.getArgument("role");
        Role sourceRole = context.getSource().getTopRole();

        if(target.getTopRole().getWeight() >= sourceRole.getWeight() && !sourceRole.getID().equals(Roles.ORGANIZER.getID())) {
            return CommandResult.temporary(CommandResults.ERROR, "You do not have permissions to do that!", 3);
        }

        target.removeRole(role);
        return CommandResult.temporary(CommandResults.SUCCESS, target.getUser().getName() + " had the \"" + role.getName() + "\" role removed!", 3);
    }
}

