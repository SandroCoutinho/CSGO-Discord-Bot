package com.sandroc.discord.csgobot.utils;

public enum DefaultSetting {
    BOT_ENABLED("botEnabled", true),
    BOT_TOKEN("botToken", "NTQ3NTIxOTAyODY1MDIzMDAw.D1P2hA.K_RJC9EB8nq7KVILN-yuA44qvxA"),
    BOT_OWNERID("botOwnerId", "466822971856650260"),
    STEAM_API_KEY("steamAPIKey", "A7FD55100FEFCA6FA07908BA34029851"),
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
