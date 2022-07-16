package com.github.naterepos.vegbot.interactions;

import com.github.naterepos.vegbot.Accessor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class VoiceHolder implements Accessor {

    private static VoiceHolder instance;
    private Map<String, Instant> vcTimeMap;

    public static VoiceHolder getOrCreate() {
        if(instance == null) {
            instance = new VoiceHolder();
            instance.vcTimeMap = new HashMap<>();
        }
        return instance;
    }

    public void setUserJoined(String user) {
        vcTimeMap.put(user, Instant.now());
    }

    public void setUserLeft(String user) {
        vcTimeMap.remove(user);
    }

    public int getMinutesInVC(String user) {
        return (int) (vcTimeMap.getOrDefault(user, Instant.now()).until(Instant.now(), ChronoUnit.MINUTES));
    }
}
