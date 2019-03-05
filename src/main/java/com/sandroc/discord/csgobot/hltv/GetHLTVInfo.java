package com.sandroc.discord.csgobot.hltv;

import com.google.gson.Gson;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.hltv.gson.Match;
import com.sandroc.discord.csgobot.hltv.gson.News;

import java.io.IOException;

public class GetHLTVInfo {
    private static ILanding landing;

    public GetHLTVInfo(ILanding landing) {
        GetHLTVInfo.landing = landing;
    }

    public Match[] getResults() {
        try {
            String response = landing.getMethods().readUrl(Constants.GET_RESULTS);

            return new Gson().fromJson(response, Match[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Match getResultForTeam(String teamName) {
        Match[] matches = getResults();

        for (Match match : matches) {
            if (match.teamOne.name.equalsIgnoreCase(teamName)
                    || match.teamTwo.name.equalsIgnoreCase(teamName)) {
                return match;
            }
        }

        return null;
    }

    public News[] getLatestNews() {
        try {
            String response = landing.getMethods().readUrl(Constants.GET_NEWS);

            return new Gson().fromJson(response, News[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public News getNewsByIndex(int index) {
        return getLatestNews()[index];
    }
}
