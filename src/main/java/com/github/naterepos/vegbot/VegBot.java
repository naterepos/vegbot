package com.github.naterepos.vegbot;

import com.github.naterepos.vegbot.command.CommandRegistry;
import com.github.naterepos.vegbot.events.ChatEvents;
import com.github.naterepos.vegbot.events.ConnectionEvents;
import com.github.naterepos.vegbot.events.VoiceEvents;
import com.github.naterepos.vegbot.resources.MySQL;
import com.github.naterepos.vegbot.resources.UserRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class VegBot extends ListenerAdapter implements Accessor {

    private static VegBot instance;
    private JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        instance = new VegBot();
        if(instance.config().reload()) {
            instance.jda = JDABuilder.createLight(instance.settings().getToken(), Arrays.asList(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS))
                    .setActivity(Activity.playing("Type " + instance.settings().getCommandKey() + "help to get started"))
                    .addEventListeners(new ChatEvents())
                    .addEventListeners(new VoiceEvents())
                    .addEventListeners(new ConnectionEvents())
                    .build().awaitReady();
            UserRegistry.getOrCreate();
            MySQL.getOrCreate();
            CommandRegistry.getOrCreate();
        } else {
            System.err.println("\n\nConfiguration has not been setup. Please edit the Settings.properties to continue...\n\n");
            System.exit(-1);
        }
    }

    public static VegBot getInstance() {
        return instance;
    }

    public JDA getJDA() {
        return jda;
    }
}