package com.sandroc.discord.csgobot.utils;

public enum DefaultSetting {
    BOT_ENABLED("botEnabled", false),
    BOT_TOKEN("botToken", "BOT_TOKEN"),
    STEAM_API_KEY("steamAPIKey", "STEAM_API_KEY");

    private String key;
    private Object value;

    DefaultSetting(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
