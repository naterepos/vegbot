package com.github.higgs.vegbot;

import com.github.higgs.vegbot.command.CommandRegistry;
import com.github.higgs.vegbot.events.ChatEvents;
import com.github.higgs.vegbot.events.ConnectionEvents;
import com.github.higgs.vegbot.interactions.InteractionRegistry;
import com.github.higgs.vegbot.resources.MySQL;
import com.github.higgs.vegbot.resources.UserRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class VegBot extends ListenerAdapter {

    private static VegBot instance;
    private JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        instance = new VegBot();
        instance.jda = JDABuilder.createLight(VegBot.<String>getSetting("Token").orElseThrow(LoginException::new), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .setActivity(Activity.playing("Type ?help to get started"))
                .addEventListeners(new ChatEvents())
                .addEventListeners(new ConnectionEvents())
                .build().awaitReady();
        UserRegistry.getOrCreate();
        MySQL.getOrCreate();
        CommandRegistry.getOrCreate();
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getSetting(String setting) {
        Properties setup = new Properties();
        try {
            setup.load(VegBot.class.getResourceAsStream("/Setup.properties"));
            Properties settings = new Properties();
            settings.load(new FileInputStream(setup.getProperty("TestingPath")));
            //TODO: add ability to toggle boolean to make config file pop up in same place as jar. Otherwise, we want to keep it in normal config location for runnables

            if(settings.containsKey(setting)) {
                String value = settings.getProperty(setting);

                if(value.matches("^[-+]?\\d+$")) {
                    long longNum = Long.parseLong(value);
                    if(longNum > Integer.MAX_VALUE) {
                        return Optional.of((T) ((Long) longNum));
                    } else {
                        return Optional.of((T) ((Integer) Integer.parseInt(value)));
                    }
                } else if(value.matches("[+-]?([0-9]*[.])?[0-9]+")) {
                    return Optional.of((T) ((Double) Double.parseDouble(value)));
                } else if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    return Optional.of((T) Boolean.valueOf(setting));
                } else {
                    return Optional.of((T) value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Guild getGuild() {
        return VegBot.getJDA().getGuildById(VegBot.<Long>getSetting("GuildID").orElse(0L));
    }

    public static CommandRegistry getCommands() {
        return CommandRegistry.getOrCreate();
    }

    public static MySQL getSQL() {
        return MySQL.getOrCreate();
    }

    public static UserRegistry getUsers() {
        return UserRegistry.getOrCreate();
    }

    public static InteractionRegistry getInteractions() {
        return InteractionRegistry.getOrCreate();
    }

    public static JDA getJDA() {
        return instance.jda;
    }
}