package com.sandroc.discord.csgobot.steam;

public class URLConstants {

    public static final String GET_USER_STATS   = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=%s&steamid=%s";
    public static final String GET_USER_STEAMID = "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=%s&vanityurl=%s";
    public static final String GET_STEAM_INFO   = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s";
}
