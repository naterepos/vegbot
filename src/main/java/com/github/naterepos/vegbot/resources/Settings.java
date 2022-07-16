package com.github.naterepos.vegbot.resources;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
@ConfigSerializable
public class Settings {
    @Setting("GuildID") private long guildID = 0;
    @Setting("Token") private String token = "";
    @Setting("CommandKey") private String commandKey = "?";
    @Setting("SQL") private Database database = new Database();
    @Setting("Points") private Points points = new Points();

    @ConfigSerializable
    public static class Points {
        @Setting("WorthSendMessage") private int worthSendMessage = 5;
        @Setting("WorthVoiceCall") private int worthVoiceCall = 10;
        @Setting("MinutesRequiredForVCPoints") private int minutesRequiredVC = 10;
        @Setting("ChannelBlackListVC") private List<String> bannedChannels = Collections.singletonList("");
    }

    @ConfigSerializable
    public static class Database {
        @Setting("Host") private String host = "localhost:3306";
        @Setting("Database") private String database = "";
        @Setting("Username") private String username = "";
        @Setting("Password") private String password = "";

        public boolean isLoaded() {
            return host != null && database != null && username != null && password != null &&
                    !host.equals("") && !database.equals("") && !username.equals("") && !password.equals("");
        }
    }

    public boolean isAllowedPointChannel(String channel) {
        return !points.bannedChannels.contains(channel);
    }

    public int getMinutesRequiredForVCPoints() {
        return points.minutesRequiredVC;
    }

    public int getWorthForSendMessage() {
        return points.worthSendMessage;
    }

    public int getWorthForVoiceCall() {
        return points.worthVoiceCall;
    }

    public long getGuildID() {
        return guildID;
    }

    public String getToken() {
        return token;
    }

    public Connection getSQLConnection() throws SQLException {
        if(database.isLoaded()) {
            return DriverManager.getConnection("jdbc:mysql://" + database.host + "/" + database.database + "?characterEncoding=latin1", database.username, database.password);
        }
        throw new SQLException("No information has been setup in the configuration");
    }

    public String getCommandKey() {
        return commandKey;
    }
}
