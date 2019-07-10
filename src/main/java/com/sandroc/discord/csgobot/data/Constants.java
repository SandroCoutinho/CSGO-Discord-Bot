package com.sandroc.discord.csgobot.data;

public class Constants {

    public static final String[] ACTIVE_MAP_POOL = { "de_vertigo", "de_dust2", "de_inferno", "de_mirage", "de_nuke", "de_overpass", "de_train" };

    public static final String STEAM_API        = "https://api.steampowered.com/";
    public static final String GET_USER_STATS   = STEAM_API + "ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=%s&steamid=%s";
    public static final String GET_USER_STEAMID = STEAM_API + "ISteamUser/ResolveVanityURL/v0001/?key=%s&vanityurl=%s";
    public static final String GET_STEAM_INFO   = STEAM_API + "ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s";

    public static final String HLTV_API    = "https://hltv-api-bot.herokuapp.com/";
    public static final String GET_RESULTS = HLTV_API + "results";
    public static final String GET_NEWS    = HLTV_API + "news";

    public static final String FACEIT_API  = "https://open.faceit.com/data/v4/";
    public static final String SEARCH_USER = FACEIT_API + "search/players?nickname=%s&game=csgo&offset=0&limit=1";
}
