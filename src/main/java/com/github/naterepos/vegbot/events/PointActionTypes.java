package com.github.naterepos.vegbot.events;

import com.github.naterepos.vegbot.VegBot;

import java.util.function.Function;

public enum PointActionTypes {

    SENT_CHAT(amount -> (amount * VegBot.getInstance().settings().getWorthForSendMessage())),
    SPENT_TIME_IN_VC(amount -> (amount * VegBot.getInstance().settings().getWorthForVoiceCall()));

    private final Function<Integer, Integer> worth;

    PointActionTypes(Function<Integer, Integer> worth) {
        this.worth = worth;
    }

    public int getWorth(int amount) {
        return worth.apply(amount);
    }
}
