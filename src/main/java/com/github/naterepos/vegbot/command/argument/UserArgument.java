package com.github.naterepos.vegbot.command.argument;

import com.github.naterepos.vegbot.Accessor;
import com.github.naterepos.vegbot.user.VegUser;

import java.util.Optional;

public class UserArgument extends Argument implements Accessor {

    public UserArgument(String key) {
        super(key);
    }

    @Override
    public VegUser formatInputIfValid(String rawInput) {
        try {
            long id = Long.parseLong(rawInput);
            Optional<VegUser> user = users().getUser(id);
            if(user.isPresent()) {
                return user.get();
            }
        } catch(NumberFormatException e) {
            return users().getUser(rawInput).orElse(null);
        }
        return null;
    }
}
