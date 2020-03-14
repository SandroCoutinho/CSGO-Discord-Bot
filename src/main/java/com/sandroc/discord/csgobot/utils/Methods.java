package com.sandroc.discord.csgobot.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.faceit.gson.Profile;
import com.sandroc.discord.csgobot.steam.stats.csgo.CsgoStats;
import com.sandroc.discord.csgobot.steam.stats.csgo.GameStats;
import com.sandroc.discord.csgobot.steam.stats.steam.SteamInfo;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.*;

public class Methods {
    private final ILanding landing;

    public Methods(ILanding landing) {
        this.landing = landing;
    }

    public String capitalizeSentence(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private int minutesToHours(String minutes) {
        return Integer.parseInt(minutes) / 60 / 60;
    }

    public String readUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL           url           = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlString.startsWith(Constants.FACEIT_API)) {
                urlConnection.setRequestProperty("Authorization", "Bearer " + FileUtils.getProperty("default", "faceItKey"));
            }
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

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

    public File getFileForMap(String map) {
        return new File("cache/" + map + ".jpg");
    }

    public File getFileForCoinflip(double randomNumber) {
        if (randomNumber < 0.5) {
            return new File("cache/counter-terrorist.png");
        } else {
            return new File("cache/terrorist.png");
        }
    }

    public File getFileForVeto(boolean ban) {
        if (ban) {

            return new File("cache/ban.png");
        } else {
            return new File("cache/pick.png");
        }
    }

    public EmbedBuilder buildRandomMap(String map) {
        return new EmbedBuilder()
                .setTitle("Random Map")
                .setImage("attachment://" + map + ".jpg")
                .setDescription("Your random map is " + capitalizeSentence(map.substring("de_".length())) + ".");
    }

    public EmbedBuilder buildCoinflip(double randomNumber) {
        return new EmbedBuilder()
                .setTitle("Coinflip")
                .setColor(randomNumber < 0.5 ? Color.BLUE : Color.ORANGE)
                .setImage("attachment://" + (randomNumber < 0.5 ? "counter-terrorist" : "terrorist") + ".png")
                .setDescription("You throw a coin in the air and it lands on " + (randomNumber < 0.5 ? "CT" : "T") + " sided.");
    }

    public EmbedBuilder buildSteamInfo(CommandEvent event, String username) {
        EmbedBuilder embedBuilder = null;

        try {
            Map<String, String> csgoStats  = new HashMap<>();
            SteamInfo           steamInfo  = Objects.requireNonNull(this.landing.getSteamInfo().getSteamInfo(event, username)).players[0];
            CsgoStats           csgoStats1 = this.landing.getSteamInfo().getCSGOStats(username);
            Profile             profile    = this.landing.getFaceItInfo().getProfiles(this.landing.getSteamInfo().getSteamId(username));

            if (steamInfo != null) {

                if (csgoStats1 != null) {
                    for (int i = 0; i < csgoStats1.stats.length; i++) {
                        csgoStats.put(csgoStats1.stats[i].name, csgoStats1.stats[i].value);
                    }
                }

                embedBuilder = new EmbedBuilder();

                embedBuilder.setTitle(steamInfo.personaname);
                embedBuilder.setColor(Color.BLUE);
                embedBuilder.setThumbnail(steamInfo.avatarfull);

                embedBuilder.setDescription("CSGO Stats");

                if (profile.items.length > 0) {
                    embedBuilder.addField("Country", ":flag_" + profile.items[0].country.toLowerCase() + ":", true);
                    embedBuilder.addField("FaceIt Level", profile.items[0].games[0].skillLevel, true);
                }
                if (!csgoStats.isEmpty()) {
                    embedBuilder.addField("Total MVPs", formatNumber(csgoStats.get("total_mvps")), true);
                    embedBuilder.addField("Game Time", minutesToHours(csgoStats.get("total_time_played")) + " Hrs", true);
                    embedBuilder.addField("Total Kills", formatNumber(csgoStats.get("total_kills")), true);
                    embedBuilder.addField("Total Deaths", formatNumber(csgoStats.get("total_deaths")), true);
                    embedBuilder.addField("Total Damage Done", formatNumber(csgoStats.get("total_damage_done")), true);
                    embedBuilder.addField("Total Knife Kills", formatNumber(csgoStats.get("total_kills_knife")), true);
                    embedBuilder.addField("Total Bombs Planted", formatNumber(csgoStats.get("total_planted_bombs")), true);
                    embedBuilder.addField("Total Bombs Defused", formatNumber(csgoStats.get("total_defused_bombs")), true);
                    embedBuilder.addField("Total Pistol Rounds Won", formatNumber(csgoStats.get("total_wins_pistolround")), true);
                    embedBuilder.addField("Most Played Map", getMostPlayedMap(csgoStats1.stats), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return embedBuilder;
    }

    public boolean isMatchingNumberOfArgs(String[] command, String[] items) {
        return items[0].length() > 1 && items.length == command.length;
    }

    public boolean containsValue(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.equals(targetValue)) {
                return true;
            }
        }

        return false;
    }
}
