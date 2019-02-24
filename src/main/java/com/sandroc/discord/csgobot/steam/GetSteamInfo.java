package com.sandroc.discord.csgobot.steam;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sandroc.discord.csgobot.steam.stats.csgo.CSGOResponse;
import com.sandroc.discord.csgobot.steam.stats.csgo.CsgoStats;
import com.sandroc.discord.csgobot.steam.stats.csgo.PlayerStats;
import com.sandroc.discord.csgobot.steam.stats.steam.Players;
import com.sandroc.discord.csgobot.steam.stats.steam.SteamResponse;
import com.sandroc.discord.csgobot.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GetSteamInfo {

    private static String getSteamId(String name) {
        try {
            String json = readUrl(URLConstants.GET_USER_STEAMID + name);

            Gson         gson       = new Gson();
            CSGOResponse gsonOutput = gson.fromJson(json, CSGOResponse.class);

            if (gsonOutput.response.success == 1) {
                return gsonOutput.response.steamid;
            }
        } catch (Exception e) {
            return "Failed";
        }

        return name;
    }

    public static Players getSteamInfo(CommandEvent event, String steamID) {
        try {
            if (!steamID.matches("[0-9]+")) {
                steamID = getSteamId(steamID);
            }
            if (steamID.equalsIgnoreCase("failed")) {
                MessageUtils.sendMessage(event, "Steam is currently down!");
                return null;
            }

            String json = readUrl(URLConstants.GET_STEAM_INFO + steamID);

            Gson gson = new Gson();

            return gson.fromJson(json, SteamResponse.class).response;
        } catch (Exception ignored) {
        }

        return null;
    }

    public static CsgoStats getCSGOStats(String name) {
        try {
            if (!name.matches("[0-9]+")) {
                name = getSteamId(name);
            }

            String json = readUrl(URLConstants.GET_USER_STATS + name);

            Gson gson = new Gson();
            return gson.fromJson(json, PlayerStats.class).playerstats;
        } catch (IOException ignored) {
        }

        return null;
    }

    private static String readUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int           read;
            char[]        chars  = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return urlString;
    }
}