package com.github.naterepos.vegbot.interactions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InteractionRegistry {

    private static InteractionRegistry instance;
    private Map<String, Interaction> map;

    public static InteractionRegistry getOrCreate() {
        if(instance == null) {
            instance = new InteractionRegistry();
            instance.map = new HashMap<>();
        }
        return instance;
    }

    public void addUser(String userID, Interaction interaction) {
        Interaction previousActive = map.getOrDefault(userID, null);
        if(previousActive != null) {
            previousActive.finishAndCleanup(false, false);
        }

        map.put(userID, interaction);
    }

    public Optional<Interaction> getActiveInteraction(String userID) {
        return Optional.ofNullable(map.getOrDefault(userID, null));
    }

    public void removeUser(String id) {
        map.remove(id);
    }
}
