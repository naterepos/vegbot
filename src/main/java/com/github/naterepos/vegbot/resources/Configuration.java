package com.github.naterepos.vegbot.resources;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

    private static transient Configuration instance;
    private Settings settings;

    public static Configuration getOrCreate() {
        if(instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public boolean reload() {
        boolean isSetup = true;
        try {
            Path path = Paths.get("Settings.conf");
            if(Files.notExists(path)) {
                Files.createFile(path);
                isSetup = false;
            }
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(Paths.get("Settings.conf")).prettyPrinting(true).build();
            CommentedConfigurationNode root = loader.load();
            settings = root.get(Settings.class);
            root.set(Settings.class, settings);
            loader.save(root);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return isSetup;
    }

    public Settings getSettings() {
        return settings;
    }
}
