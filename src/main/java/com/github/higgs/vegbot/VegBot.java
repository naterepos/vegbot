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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class VegBot extends ListenerAdapter {

    private static VegBot instance;
    private JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        instance = new VegBot();
        try {
            startUpRemote();
        } catch(LoginException e) {
            System.exit(1);
        }
        instance.jda = JDABuilder.createLight(VegBot.getSetting("Token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .setActivity(Activity.playing("Type ?help to get started"))
                .addEventListeners(new ChatEvents())
                .addEventListeners(new ConnectionEvents())
                .build().awaitReady();
        UserRegistry.getOrCreate();
        MySQL.getOrCreate();
        CommandRegistry.getOrCreate();
    }

    private static void startUpRemote() throws LoginException {
        Properties setup = new Properties();
        try {
            setup.load(VegBot.class.getResourceAsStream("/Setup.properties"));
            Properties settings = new Properties();
            if(setup.getProperty("UseCustomPath").equalsIgnoreCase("true")) {
                if(Files.notExists(Paths.get(setup.getProperty("ConfigPath")))) {
                    Path configPath = Files.createFile(Paths.get(setup.getProperty("ConfigPath")));
                    settings.load(new FileInputStream(setup.getProperty("ConfigPath")));
                    settings.setProperty("GuildID", "server_id");
                    settings.setProperty("Token", "bot_token");
                    settings.setProperty("MySQLHost", "host_name");
                    settings.setProperty("MySQLDatabase", "database_name");
                    settings.setProperty("MySQLUsername", "username");
                    settings.setProperty("MySQLPassword", "password");
                    settings.store(new FileOutputStream(configPath.toFile()), "VegBot Configuration. If you have an all number field, put [STRING] before it. I.E: \"password=[STRING]012345\"");
                    System.err.println("\n\nConfiguration has not been setup. Please edit the Settings.properties to continue...\n\n");
                    throw new LoginException();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSetting(String setting) {
        Properties setup = new Properties();
        try {
            setup.load(VegBot.class.getResourceAsStream("/Setup.properties"));
            Properties settings = new Properties();
            if(setup.getProperty("UseCustomPath").equalsIgnoreCase("true")) {
                settings.load(new FileInputStream(setup.getProperty("ConfigPath")));
            } else {
                settings.load(new FileInputStream(setup.getProperty("TestingPath")));
            }

            if(settings.containsKey(setting)) {
                String value = settings.getProperty(setting);
                // Handles strings who are auto parsed as ints
                if(value.startsWith("[STRING]")) {
                    return (T) value.substring(8);
                }
                try {
                    if(value.matches("^[-+]?\\d+$")) {
                        long longNum = Long.parseLong(value);
                        if(longNum > Integer.MAX_VALUE) {
                            return (T) ((Long) longNum);
                        } else {
                            return (T) ((Integer) Integer.parseInt(value));
                        }
                    } else if(value.matches("[+-]?([0-9]*[.])?[0-9]+")) {
                        return (T) ((Double) Double.parseDouble(value));
                    } else if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        return (T) Boolean.valueOf(setting);
                    } else {
                        return (T) value;
                    }
                } catch(ClassCastException e) {
                    return (T) value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) "";
    }

    public static Guild getGuild() {
        return VegBot.getJDA().getGuildById(VegBot.<Long>getSetting("GuildID"));
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