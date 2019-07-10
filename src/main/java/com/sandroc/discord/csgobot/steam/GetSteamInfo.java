package com.sandroc.discord.csgobot.steam;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.steam.stats.csgo.CSGOResponse;
import com.sandroc.discord.csgobot.steam.stats.csgo.CsgoStats;
import com.sandroc.discord.csgobot.steam.stats.csgo.PlayerStats;
import com.sandroc.discord.csgobot.steam.stats.csgo.SteamID;
import com.sandroc.discord.csgobot.steam.stats.steam.Players;
import com.sandroc.discord.csgobot.steam.stats.steam.SteamResponse;
import com.sandroc.discord.csgobot.utils.FileUtils;

import java.io.IOException;

public class GetSteamInfo {
    private ILanding landing;

    public GetSteamInfo(ILanding landing) {
        this.landing = landing;
    }

    public String getSteamId(String name) {
        try {
            String  response      = this.landing.getMethods().readUrl(String.format(Constants.GET_USER_STEAMID, FileUtils.getProperty("default", "steamAPIKey"), name));
            SteamID steamResponse = new Gson().fromJson(response, CSGOResponse.class).response;

            if (steamResponse.success == 1) {
                return steamResponse.steamid;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return "Failed";
        }

        return name;
    }

    public Players getSteamInfo(CommandEvent event, String steamID) {
        try {
            if (!steamID.matches("[0-9]+")) {
                steamID = getSteamId(steamID);
            }
            if (steamID.equalsIgnoreCase("failed")) {
                this.landing.getMessageUtils().sendMessage(event, "Steam is currently down!");
                return null;
            }

            String response = this.landing.getMethods().readUrl(String.format(Constants.GET_STEAM_INFO, FileUtils.getProperty("default", "steamAPIKey"), steamID));

            return new Gson().fromJson(response, SteamResponse.class).response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public CsgoStats getCSGOStats(String name) {
        try {
            if (!name.matches("[0-9]+")) {
                name = getSteamId(name);
            }

            String json = this.landing.getMethods().readUrl(String.format(Constants.GET_USER_STATS, FileUtils.getProperty("default", "steamAPIKey"), name));

            return new Gson().fromJson(json, PlayerStats.class).playerstats;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}