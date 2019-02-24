package com.sandroc.discord.csgobot.utils;

public enum DefaultSetting {
    BOT_ENABLED("botEnabled", true),
    BOT_TOKEN("botToken", "BOT_TOKEN"),
    BOT_OWNERID("botOwnerId", "466822971856650260"),
    STEAM_API_KEY("steamAPIKey", "STEAM_API_KEY"),
    BOT_COMMAND_PREFIX("botCommandPrefix", "!");

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
