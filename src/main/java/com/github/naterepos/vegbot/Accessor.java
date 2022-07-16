package com.github.naterepos.vegbot;

import com.github.naterepos.vegbot.command.CommandRegistry;
import com.github.naterepos.vegbot.interactions.InteractionRegistry;
import com.github.naterepos.vegbot.interactions.VoiceHolder;
import com.github.naterepos.vegbot.resources.Configuration;
import com.github.naterepos.vegbot.resources.MySQL;
import com.github.naterepos.vegbot.resources.Settings;
import com.github.naterepos.vegbot.resources.UserRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public interface Accessor {

    default VegBot app() {
        return VegBot.getInstance();
    }

    default UserRegistry users() {
        return UserRegistry.getOrCreate();
    }

    default MySQL data() {
        return MySQL.getOrCreate();
    }

    default Guild guild() {
        return jda().getGuildById(settings().getGuildID());
    }

    default VoiceHolder voiceHolder() {
        return VoiceHolder.getOrCreate();
    }

    default Configuration config() {
        return Configuration.getOrCreate();
    }

    default CommandRegistry commands() {
        return CommandRegistry.getOrCreate();
    }

    default InteractionRegistry interactions() {
        return InteractionRegistry.getOrCreate();
    }

    default Settings settings() {
        return Configuration.getOrCreate().getSettings();
    }

    default JDA jda() {
        return app().getJDA();
    }
}
