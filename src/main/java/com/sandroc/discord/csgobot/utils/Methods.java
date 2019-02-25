package com.sandroc.discord.csgobot.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.steam.GetSteamInfo;
import com.sandroc.discord.csgobot.steam.stats.csgo.GameStats;
import com.sandroc.discord.csgobot.steam.stats.steam.SteamInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.text.NumberFormat;
import java.util.*;

public class Methods {

    public String capitalizeSentence(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private int minutesToHours(String minutes) {
        return Integer.parseInt(minutes) / 60 / 60;
    }

    private String getMostPlayedMap(GameStats[] stats) {
        HashMap<String, Integer> maps         = new HashMap<>();
        String                   totalWinsMap = "total_wins_map_";

        for (GameStats stats1 : stats) {
            if (stats1.name.startsWith(totalWinsMap)) {
                maps.put(stats1.name, Integer.valueOf(stats1.value));
            }
        }

        int    maxValue = Collections.max(maps.values());
        String message  = getKeysByValue(maps, maxValue).toString().substring(totalWinsMap.length() + 1);

        return capitalizeSentence(message.substring("de_".length()).replace("]", " (" + formatNumber(String.valueOf(maxValue)) + ")"));
    }

    private <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private String formatNumber(final String message) {
        double             start = Double.parseDouble(message);
        final NumberFormat nf    = NumberFormat.getIntegerInstance();
        if (start >= 1000000000) {
            return nf.format(start / 1000000000) + "B";
        } else if (start >= 1000000) {
            return nf.format(start / 1000000) + "M";
        } else if (start >= 1000) {
            return nf.format(start);
        }

        return String.valueOf((int) start);
    }

    public String getImageForMap(String mapName) {
        switch (mapName.toLowerCase()) {
            case "de_cache":
                return "https://i.imgur.com/QzJmbXn.jpg";
            case "de_dust2":
                return "https://i.imgur.com/CBPdOtM.jpg";
            case "de_inferno":
                return "https://i.imgur.com/FkufBWo.jpg";
            case "de_mirage":
                return "https://i.imgur.com/HdWXp2d.jpg";
            case "de_nuke":
                return "https://i.imgur.com/wGNdBmz.jpg";
            case "de_overpass":
                return "https://i.imgur.com/pHHc7ck.jpg";
            case "de_train":
                return "https://i.imgur.com/3IZK0yH.jpg";
            default:
                return "";
        }
    }

    public EmbedBuilder buildRandomMap() {
        String map = Constants.ACTIVE_MAP_POOL[(int) Math.floor(Math.random() * Constants.ACTIVE_MAP_POOL.length)];

        return new EmbedBuilder()
                .setTitle("Random Map")
                .setImage(getImageForMap(map))
                .setDescription("Your random map is " + capitalizeSentence(map.substring("de_".length())) + ".");
    }

    public EmbedBuilder buildCoinflip() {
        double randomNumber = Math.random();

        return new EmbedBuilder()
                .setTitle("Coinflip")
                .setColor(randomNumber < 0.5 ? Color.BLUE : Color.ORANGE)
                .setImage(randomNumber < 0.5 ? Constants.CT_COIN_URL : Constants.T_COIN_URL)
                .setDescription("You throw a coin in the air and it lands on " + (randomNumber < 0.5 ? "CT" : "T") + " sided.");
    }

    public EmbedBuilder buildSteamInfo(CommandEvent event, String username) {
        EmbedBuilder embedBuilder = null;
        try {
            HashMap<String, String> csgoStats = new HashMap<>();
            SteamInfo               steamInfo = Objects.requireNonNull(GetSteamInfo.getSteamInfo(event, username)).players[0];
            GameStats[]             gameStats = Objects.requireNonNull(GetSteamInfo.getCSGOStats(username)).stats;

            for (GameStats stats : gameStats) {
                csgoStats.put(stats.name, stats.value);
            }

            embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(steamInfo.personaname);
            embedBuilder.setColor(Color.BLUE);
            embedBuilder.setThumbnail(steamInfo.avatarfull);

            embedBuilder.setDescription("CSGO Stats");

            embedBuilder.addField("Total MVPs", formatNumber(csgoStats.get("total_mvps")), true);
            embedBuilder.addField("Game Time", minutesToHours(csgoStats.get("total_time_played")) + " Hrs", true);
            embedBuilder.addField("Total Kills", formatNumber(csgoStats.get("total_kills")), true);
            embedBuilder.addField("Total Deaths", formatNumber(csgoStats.get("total_deaths")), true);
            embedBuilder.addField("Total Damage Done", formatNumber(csgoStats.get("total_damage_done")), true);
            embedBuilder.addField("Total Knife Kills", formatNumber(csgoStats.get("total_kills_knife")), true);
            embedBuilder.addField("Total Bombs Planted", formatNumber(csgoStats.get("total_planted_bombs")), true);
            embedBuilder.addField("Total Bombs Defused", formatNumber(csgoStats.get("total_defused_bombs")), true);
            embedBuilder.addField("Total Pistol Rounds Won", formatNumber(csgoStats.get("total_wins_pistolround")), true);
            embedBuilder.addField("Most Played Map", getMostPlayedMap(gameStats), true);
        } catch (Exception ignored) {
        }

        return embedBuilder;
    }
}
