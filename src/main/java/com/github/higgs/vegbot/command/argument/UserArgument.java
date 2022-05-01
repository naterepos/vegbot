package com.github.higgs.vegbot.command.argument;

import com.github.higgs.vegbot.VegBot;
import com.github.higgs.vegbot.user.VegUser;

import java.util.Optional;

public class UserArgument extends Argument {

    public UserArgument(String key) {
        super(key);
    }

    @Override
    public VegUser formatInputIfValid(String rawInput) {
        try {
            long id = Long.parseLong(rawInput);
            Optional<VegUser> user = VegBot.getUsers().getUser(id);
            if(user.isPresent()) {
                return user.get();
            }
        } catch(NumberFormatException e) {
            return VegBot.getUsers().getUser(rawInput).orElse(null);
        }
        return null;
    }
}
