package com.sandroc.discord.csgobot.steam;

import com.sandroc.discord.csgobot.utils.FileUtils;

public class URLConstants {

    public static final String GET_USER_STATS   = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=" +
            FileUtils.getProperty("default", "steamAPIKey") + "&steamid=";
    public static final String GET_USER_STEAMID = "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" +
            FileUtils.getProperty("default", "steamAPIKey") + "&vanityurl=";
    public static final String GET_STEAM_INFO   = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" +
            FileUtils.getProperty("default", "steamAPIKey") + "&steamids=";
}
